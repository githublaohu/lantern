# Dubbo injvm + 数据库实时 RBAC

本模式面向 Spring Boot 2.x / Spring MVC（javax）。回归宿主使用 Java 17、Boot 2.7.18、Dubbo 3.2.16、MyBatis 2.3.2，与当前 EventMesh Console 的版本对齐；不适用于 Boot 3 的 jakarta Servlet。

## 业务项目需要做什么

引入 `cn.lampup:lantern-plugins-springmvc:0.0.1-SNAPSHOT` 和 `cn.lampup:lantern-service-core-provider:0.0.1-SNAPSHOT`，使用宿主已有的数据源和 MyBatis。Provider 的普通 JAR 可作为依赖；独立运行包使用 `-exec.jar`。Provider 演示数据库、端口配置只在独立入口的 `lantern-provider` profile 中加载，不污染业务宿主。

以下配置与 EventMesh 现有 `application-lantern.yml` 一致，激活 `lantern` profile 即可。EventMesh 已有的 Lantern 配置包扫描也可以继续使用，不会重复创建 Bean。新宿主不需要扫描 Lantern 包，也不用写用户、角色、权限 Mapper 或 Dubbo 装配代码。

```yaml
lantern:
  authenticationConfig:
    enable: true
    authenticationFlowConfig:
      systemId: "915"
      includePaths:
        - /user/cluster/queryClusterByOrganizationIdAndType
dubbo:
  application:
    name: ${spring.application.name:eventmesh-dashboard}
    qos-enable: false
    metadata-service-protocol: injvm
  registry:
    address: N/A
  protocol:
    name: injvm
    serialization: fastjson2
  provider:
    scope: local
    group: lantern-authentication
  consumer:
    scope: local
    group: lantern-authentication
    retries: 0
    parameters:
      serialization: fastjson2
```

Lantern 默认补齐以上 Dubbo 设置、`Authorization: Bearer <token>` 读取设置、鉴权与策略服务选择，并自动提供账号密码登录和退出接口。宿主显式配置优先，例如自定义 Dubbo application name、配套的 provider/consumer group。`systemId` 和 `includePaths` 必须显式提供，缺失时启动失败。只有 `includePaths` 命中的业务接口受保护；扩大保护范围时，必须同步配置对应的数据库 HTTP 资源。Lantern 自带登录和退出接口自行处理身份检查，不需要额外配置资源策略，即使保护 `/**` 也可正常登录。

## 登录、会话与退出

数据库模式复用原来的 `LanternAuthOperate → HandlerExecute → CreateTokenAuthHandler → TokenService`。账号密码认证类从独立登录应用移到 core，独立应用通过 Bean 继续复用；同时修正服务未注入、比较错误密码对象、返回请求伪造身份，以及认证失败后继续执行成功处理的问题。Token 处理器新增一个保存扩展点：原实现继续保存 Redis，数据库模式仅替换保存动作，使用原 UUID 生成器，不创建 Redis 连接。

默认启用 `lantern.loginConfig.enable=true`、`sessionStore=database`，会话有效期通过 `lantern.loginConfig.token.tokenExpire` 配置，单位秒，默认 604800（七天）。设置 `lantern.loginConfig.enable=false` 可以只使用鉴权，不加载登录和退出接口。内置数据库登录使用 header Token，默认响应和后续请求都使用 `Authorization: Bearer <token>`。

假设数据库已有系统 915 的用户 alice，盐和密码摘要沿用已有注册流程的格式 `MD5(ui_salt + 原始密码)`，并已授予业务接口权限。按 EventMesh 开发环境端口和访问前缀调用：

```bash
curl -i -X POST 'http://localhost:9898/eventmesh/dashboard/account/first/accountLogin' \
  -H 'Content-Type: application/json' \
  -d '{"uiName":"alice","uiPassword":"alice的密码"}'
```

成功时返回 HTTP 200，Token 位于响应头 `Authorization` 和响应体 `data.uiToken`。响应只包含数据库查出的身份和新 Token，不包含密码、盐或摘要；客户端传入的用户 ID、系统 ID、Token 均不作为登录身份依据。错误密码、不存在、禁用、删除或同系统重名用户返回 401，不保存会话、不签发 Token；数据库或保存失败返回 503。

会话直接复用现有 `user_login_record` 表：`system_id`、`ui_id` 标识用户，`ul_session_id` 保存 UUID Token，`ul_login_status=SUCCESS`，`ul_quit_time` 保存预定失效时刻。新登录不写 `user_info.ui_token`。每次鉴权检查会话尚未失效、用户仍有效，再查询当前角色权限；支持同一用户多个独立会话。原有 `user_info.ui_token` 静态 Token 入口继续兼容，其有效性仍由原字段维护。没有新增会话表或修改已有表结构。

```bash
# 用返回的 Token 访问原业务接口
curl -i -X POST 'http://localhost:9898/eventmesh/dashboard/user/cluster/queryClusterByOrganizationIdAndType' \
  -H 'Authorization: Bearer <data.uiToken>' \
  -H 'Content-Type: application/json' \
  -d '{"organizationId":1,"clusterType":"EVENTMESH"}'

# 退出当前会话，成功返回 204；该 Token 下次访问立即返回 401
curl -i -X POST 'http://localhost:9898/eventmesh/dashboard/account/logout' \
  -H 'Authorization: Bearer <data.uiToken>'
```

退出将当前 Token 对应的 `ul_quit_time` 提前到当前时间，其他会话保持有效；若是原静态 Token，同时清空对应的 `ui_token`。历史记录保留，不再作为有效会话。过期检查由数据库完成，无需 Redis 或定时清理任务。业务项目无需自行生成 Token，也无需添加登录 Controller。

## Dubbo 声明与调用配置

Lantern 使用 Dubbo 官方的声明方式：`@Bean + @DubboService` 声明数据库鉴权与策略服务，`@Bean + @DubboReference` 返回 `ReferenceBean<接口>` 声明引用。Spring 按 Bean 名直接取得 Dubbo 代理：`lanternDubboAuthentication` 是鉴权引用，`lanternDubboPolicy` 是策略引用；没有额外的方法转发包装类。默认认证和权限查询共用同一个鉴权代理，仅在显式配置两个不同服务时保留原有的分流适配。

这些声明不指定 `injvm`、`scope`、group 或 URL，也不手工调用 `export()` / `refer()`；由 Dubbo 管理服务导出、代理创建与销毁。本地调用由上面的 YAML 配置选择，宿主配置优先于 Lantern 的默认属性。切换远程模式时，还需要配套配置协议、服务地址发现和 provider/consumer scope。

参考官方[本地调用](https://dubbo.apache.org/zh-cn/overview/mannual/java-sdk/tasks/framework/more/local-call/)与[Spring Boot 集成](https://dubbo.apache.org/zh-cn/overview/mannual/java-sdk/tasks/develop/springboot/)。

## 一次请求怎样检查权限

以 `POST /user/cluster/queryClusterByOrganizationIdAndType` 为例：

1. MVC 拦截器取得 HTTP method 和 Spring MVC 路由模板。
2. Lantern 通过 Dubbo injvm 调用自己的策略服务，实时查询 `resources`：`system_id=915`、`resource_type=HTTP`、`resource_operate=POST`、`resource_name=该路由`。
3. 根据 `resource_conditions` 决定检查方式：

| 值 | 行为 |
| --- | --- |
| `PUBLIC` | 不要求 Token，允许匿名访问 |
| `USER` | 校验 Token 和有效用户，不要求角色授权 |
| `ROLE` | 校验 Token，再检查用户 → 用户角色关联 → 角色 → 角色资源关联 → 当前资源 |

4. 非匿名模式通过 injvm 查有效的数据库登录会话（兼容原 `user_info.ui_token`），要求同一 `system_id`、未删除、`ui_allow_login=ACTIVE`，且恰好匹配一个用户。Token 区分大小写，重复 Authorization 头或重复 Token 用户均不接受。
5. `ROLE` 检查 `user_role_relation.urr_type=USER`、各表未删除、角色与资源系统一致、所有有效期满足条件。授权查询只使用认证得到的用户 ID，忽略请求伪造的 ID。
6. 通过后把用户放入请求上下文，覆盖 DTO 中 `@UserInjection("uiId")` 字段；请求完成和开始异步处理时清理上下文。未登录请求的可空注入字段置空，不能保留客户端伪造值。

策略缺失或资源失效返回 403；非匿名接口 Token 缺失/错误返回 401；已认证但无角色权限返回 403；重复策略、非法策略值或数据库故障返回 503，不会放行。路由模板 `/orders/{id}` 使用模板本身入库，method 和路由区分大小写，不做数据库路径通配匹配。数据库策略与旧 YAML 的 `notAuthentication` / `userAuthentication` 规则不能混用。

每次检查都走数据库，不加载全量权限，不启动缓存同步定时任务，不使用 MyBatis 查询缓存。撤销关联、禁用用户、退出会话或修改策略后，下一次请求重新检查。数据库模式默认不创建 Spring Redis 自动配置、基于 Redis 的 HandlerService 或鉴权缓存，也不修改宿主的 `spring.autoconfigure.exclude`。

若宿主另有 Redis 业务，显式设置 `spring.redis.*`（或 `spring.data.redis.*`），或 `lantern.authenticationConfig.redisAutoConfigurationEnabled=true` 可以保留宿主 Redis 自动配置；这不会把 Lantern 的数据库鉴权切换为缓存模式。现有手工创建的 Redis Bean 不会被删除。选择自定义鉴权服务时，本地数据库/Dubbo 默认装配会退出。

## 数据怎么维护：一个具体例子

以下演示给已有用户 123 授予上述接口权限。用户必须属于系统 915，并由 Lantern 登录接口取得有效 Token；不需要手动写入 `ui_token`。账号的盐和摘要沿用现有注册数据格式。

```sql
START TRANSACTION;
SET @user_id = 123;
SET @expires = DATE_ADD(NOW(), INTERVAL 1 YEAR);

INSERT INTO `role` (role_type_id, system_id, role_name, start_time,
                    valid_time, end_time, create_user_id, update_user_id)
VALUES (0, 915, 'cluster-reader', NOW() - INTERVAL 1 DAY,
        @expires, @expires, 0, 0);
SET @role_id = LAST_INSERT_ID();

INSERT INTO resources (system_id, resource_type, resource_name, resource_operate,
                       resource_conditions, start_time, valid_time, end_time,
                       create_user_id, update_user_id)
VALUES (915, 'HTTP', '/user/cluster/queryClusterByOrganizationIdAndType', 'POST',
        'ROLE', NOW() - INTERVAL 1 DAY, @expires, @expires, 0, 0);
SET @resource_id = LAST_INSERT_ID();

INSERT INTO user_role_relation (urr_type, user_id, role_id, start_time,
                                valid_time, end_time, create_user_id, update_user_id)
VALUES ('USER', @user_id, @role_id, NOW() - INTERVAL 1 DAY, @expires, @expires, 0, 0);

INSERT INTO resource_role_relation (resource_id, role_id, start_time,
                                    valid_time, end_time, create_user_id, update_user_id)
VALUES (@resource_id, @role_id, NOW() - INTERVAL 1 DAY, @expires, @expires, 0, 0);
COMMIT;
```

已有资源应复用，不能为相同系统、method、路由再插入一条有效策略。删除 `resource_role_relation` 对应授权后，该用户下次请求返回 403。`valid_time` 必须是未来时间；旧表默认当前时间，插入时不填写会立即失效。`end_time=create_time` 沿用旧表的“未单独设置结束时间”语义；显式设置结束时间时也必须在未来。

## 建表与已有数据库

本地数据库模式默认使用 `lantern-schema.sql` 在宿主当前数据库创建原有八张表，全部使用 `CREATE TABLE IF NOT EXISTS`，不删除数据、不创建管理员或默认授权。已存在的表不会被修改，也不负责修复旧版本表结构。

宿主可显式设置 `spring.sql.init.mode=never`，交由已有迁移工具维护；也可配置自己的 `spring.sql.init.schema-locations`。这些显式设置优先于 Lantern 默认值。鉴权只需要其中的 `user_info`、`role`、`user_role_relation`、`resources`、`resource_role_relation`，其余三表为现有模型配套表。

## 怎样运行测试

在 Lantern 根目录执行：

```bash
# 单元测试与自动装配测试，不需要 MySQL 或 Redis
mvn -Plantern-rbac-tests -pl lantern-rbac-tests -am verify \
    -Dmaven.test.skip=false -DskipTests=false

# 独立 MySQL 测试环境，13317 端口，不含 Redis
docker compose -f lantern-rbac-tests/compose.yaml up -d --wait mysql
mvn -Plantern-rbac-tests,mysql-live -pl lantern-rbac-tests -am verify \
    -Dmaven.test.skip=false -DskipTests=false
docker compose -f lantern-rbac-tests/compose.yaml down
```

测试宿主没有扫描或导入 Lantern 配置包，使用真实 Dubbo injvm，MySQL 测试读取从 EventMesh 原配置复制的测试资源。覆盖自动建表与重复初始化、共享业务连接池/SqlSessionFactory、默认及自定义 Dubbo 配置、Redis 未启用、认证与授权失败、撤权立即生效、失效时间/删除标记/跨系统隔离、路由模板、三种策略、重复策略、DTO 注入与上下文清理。新增完整的登录 → 会话入库 → 角色鉴权 → 退出/过期测试，以及错误密码不发 Token、敏感字段清理、并发会话隔离、登录关闭与全路径保护下登录可达测试；原 Redis 保存分支通过 mock 回归。额外测试 EventMesh 现有显式扫描方式与自定义鉴权服务退让。

测试模块通过可选 profile 加入，不改变默认模块列表；该 profile 只编译相关 RBAC 测试，仓库中其他历史集成测试不在本次测试范围。MySQL 测试仅在专用库中插入并清理自己的数据。

EventMesh 原有接入测试中的 5 项加载测试及 2 项 MySQL 接入测试可继续通过。原有 `dependencyAloneDoesNotLoadLanternWithoutComponentScanning` 要求“开启鉴权但不扫描时仍不装配”，与本次 starter 的目标相反，因此不属于兼容回归；对应的新行为由本仓库的无 Lantern 包扫描宿主测试覆盖。本次没有修改 EventMesh 的测试或配置。
