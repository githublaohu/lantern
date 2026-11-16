# Lantern

Lantern 提供用户登录和基于角色的权限校验。业务项目引入依赖并配置数据源、系统 ID 和保护路径后，可使用 MySQL 会话存储、数据库实时 RBAC 和 Dubbo injvm 本地调用。

## MySQL 登录与实时 RBAC

本模式面向 Spring Boot 2.x / Spring MVC（javax）。回归宿主使用 Java 17、Boot 2.7.18、Dubbo 3.2.16、MyBatis 2.3.2，与当前 EventMesh Console 的版本对齐；不适用于 Boot 3 的 jakarta Servlet。

### 业务项目需要做什么

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

### 登录、会话与退出

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

当前完整支持数据库会话模式。原 Redis 存储分支保留用于兼容，整条登录、鉴权和退出流程暂不支持仅修改 `sessionStore=redis` 切换。

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

### Dubbo 声明与调用配置

Lantern 使用 Dubbo 官方的声明方式：`@Bean + @DubboService` 声明数据库鉴权与策略服务，`@Bean + @DubboReference` 返回 `ReferenceBean<接口>` 声明引用。Spring 按 Bean 名直接取得 Dubbo 代理：`lanternDubboAuthentication` 是鉴权引用，`lanternDubboPolicy` 是策略引用；没有额外的方法转发包装类。默认认证和权限查询共用同一个鉴权代理，仅在显式配置两个不同服务时保留原有的分流适配。

这些声明不指定 `injvm`、`scope`、group 或 URL，也不手工调用 `export()` / `refer()`；由 Dubbo 管理服务导出、代理创建与销毁。本地调用由上面的 YAML 配置选择，宿主配置优先于 Lantern 的默认属性。切换远程模式时，还需要配套配置协议、服务地址发现和 provider/consumer scope。

参考官方[本地调用](https://dubbo.apache.org/zh-cn/overview/mannual/java-sdk/tasks/framework/more/local-call/)与[Spring Boot 集成](https://dubbo.apache.org/zh-cn/overview/mannual/java-sdk/tasks/develop/springboot/)。

### 一次请求怎样检查权限

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

### 数据怎么维护：一个具体例子

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

### 建表与已有数据库

本地数据库模式默认使用 `lantern-schema.sql` 在宿主当前数据库创建原有八张表，全部使用 `CREATE TABLE IF NOT EXISTS`，不删除数据、不创建管理员或默认授权。已存在的表不会被修改，也不负责修复旧版本表结构。

宿主可显式设置 `spring.sql.init.mode=never`，交由已有迁移工具维护；也可配置自己的 `spring.sql.init.schema-locations`。这些显式设置优先于 Lantern 默认值。登录与鉴权使用其中的 `user_info`、`user_login_record`、`role`、`user_role_relation`、`resources`、`resource_role_relation`，其余两表为现有模型配套表。

### 怎样运行测试

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

<details>
<summary>原始设计记录</summary>

# 要支持 电子的异步
# 认证，鉴权，两个不同的动作
# 
用户，公司，系统，部门，区域，
1. 登录的时候，可以得到公司，系统，
2. 用户登录，直接指定部门，区域？ 还是可以选择。
3. 如何切换 公司，部门，区域。系统可以直接跳转。
   1. 如果多系统登录怎么办
   2. 多系统同时有一个接口怎么处理
      1. 是代理下，六边形
      2. 还是传递系统id
   3. 如果认证系统，尤其是在网管层。通过端口？还是证书
      1. 证书与端口绑定哈。
      2. 域名可以绑定 ip，dns
      3. ip 与lvs
      4. lvs 与后端的端口
      5. 证书与端口绑定，与域名绑定
      6. 端口与空间绑定
      7. 空间与项目绑定
      8. 是不是只需要认证绑定就ok了
      9. 隔离系统之间的数据，认证数据。
         1. 每个系统使用不同的数据地址。
         2. 当前存储进行识别与认证
         3. 预计登录A系统，绕过了登录了B系统，得到的只有B系统的权限，无法等到A系统的权限。
            1. 调用A系统接口是不存在权限的
            2. 如果用户登录行为被欺骗，怎么办
               3. 短信认证
               4. 动态码认证
               5. 登录行为被拦截
                  6. 全称 tsl
         4. 用户是否可以登录所有的系统
            1.互联网 
      10. 是否可以在建立 tsl的时候识别域名

1. 本地缓存
	 1. 启动的时候全部加载
	 2. 登录之后，加载个人
	 3. etcd全部加载
	 4. 定时清楚，然后加载
2. 直接查询db
3. 调用远程接口
4. 请求redis，

权限更新如何处理


资源拥有者（拥有有那些最基础资源，有默认角色）
1. 用户
2. 用户组 
3. 部门
   1. 不同部门，职位权限不一样
      1. 部门（不同） -> 职位（相同） -> 角色（可以相同，也可以不同） -> 资源
   2. 部门拥有那些资源
4. 职位
5. 团队
6. 角色
7. 资源（资源的角色，角色有权限）
资源类型
7. 角色
8. 接口
9. 菜单。多维度菜单
10. 视图
11. 字段
12. 字段范围
13. 字段内容
14. 目录，文件权限
    1. 上传
    2. 下载
    3. 删除
    4. 查看
    5. 上传者权限
15. 网络地址或则mac
16. 支持正则表达式或则抽象匹配，亲和与反亲和。禁止的角色与资源，个人禁止，还是角色禁止，还是
PS：职权与区域双维度，问题
17. 多匹配

18. 开启 ip，mac鉴权
19. 默认开启 角色，接口鉴权。
20. 开启 区域鉴权。作为前端展示
21. 开启 部门鉴权。作为前端展示
22. 

23. 是不是需要系统，部门，区域，拥有不同权限
24. 是直接绑定
25. 角色通用，不同角色在不同系统，区域，部门是不一样的？
26. 再次申请？
27. 三种模式，
    6. 一种角色与资源通用，被部门与区域限制
    7. 一种角色通用，资源不通用
    8. 都不通用，重申申请

视图是
1. 后端渲染
2. 前端渲染
3. 预先渲染

获得用户
1. 所有接口数据
2. 所有 需要鉴权的数据

数据获得操作
1. 从数据库。登录的时候，修改权限强行退出
2. 从本地缓存（+etcd），redis。
   3. 

修改
1. 全部删除，然后刷新

技术栈
1. mybatis，sjdbc
2. 


权限审计

</details>
