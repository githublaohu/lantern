于是深入分析业务发现登录发下有一下几种：

1. 一方登录，主要是账户（用户名，手机，邮箱）与密码和验证码登录方式
2. 二方登录，因为公司内部有多套登录系统，通过拉取token进行去其他登录系统认证
3. 三方登录，使用其他平台的登录如微信，QQ，支付宝的登录系统进行登录
4. 平台登录，用户使用小程序登录。有微信小程序，企业微信小程序

深入分析业务发现用户登录之后有成功与失败两个状态：

登录成功之后
登录注册：三方登录与平台登录的用户登录，登录成功发现用户没有在我们系统注册，于是请求三方或者平台用户信息进行注册
获得用户信息：登录成功之后，必须获得用户信息
获得用户权限：内部系统需要权限认证，成功之后获取用户对应系统的权限信息
获得组织信息：成功之后不同组织的用户，在页面会进入不同组织的视图，同时在操作的时候需要绑定组织id，所以需要用去用户组织信息
登录异常消除：用户之前登录失败会记录登录失败次数，如果次数过多就拒绝登录
黑白名单：有一些用户进去的公司的黑用户名单里面，是拒绝用户登录的
登录日志记录：每个用户登录与退出都会记录
token创建：有多种token创建方式包括jwt，可以把通过放到header，cookie等地方，同时支持jwt与redis缓存操作
登录与注册成功广播：其他系统在用户成功之后需要进行一些业务操作，登录系统需要通知到它们

登录失败之后
登录异常记录：用户登录失败会进行记录，操作一定次数会限制用户在当天或一定时间内不能登录。支持用户名与client IP限制

### 关于登录系统
#### 几个关键概念
##### [HandlerService(创建Execute)](./src/main/java/com/lamp/lantern/plugins/core/login/HandlerService.java)
`HandlerService`是我们提供服务的核心,其中储存了如下几个重要的对象:
- EnvironmentContext: 提供当前处理的Servlet
- connectionClientCache: 储存Redis连接的Map, 用来提供给`AuthHandler`使用
- `handlerExecuteMap`: 储存了`HandlerExecute`的Map, 将loginConfig.systemName作为key, 每次需要使用时可以通过loginConfig获取你要使用的`HandlerExecute`
##### [HandlerExecute](./src/main/java/com/lamp/lantern/plugins/core/login/HandlerExecute.java)
用来执行登录操作的类, 每次登录时需要提供`UserInfo`(同时使用了创建`HandlerExecute`时注入的上下文信息).
- handlerList: 开启使用的`Handler`列表
- authServiceList: 开启使用的`AuthService`列表
- loginConfig
- servlet: 当前处理的Servlet
##### [AuthHandler](./src/main/java/com/lamp/lantern/plugins/core/login/AbstractAuthHandler.java)
`AuthHandler`提供登录的附加功能, 如Token的创建, 登录失败次数的记录等.
AuthHandler提供三个功能:
- `authBefore`: 在登录之前的操作, 如拒绝黑名单用户登录
- `authAfter`: 在登录之后的操作, 如记录登录日志
- `error`: 在登录异常(登录失败)的操作, 如记录登录失败次数
每个Handler里面储存着自己的Config信息。
##### AuthService

#### Config

| Config                                                                                                               | 说明                                                                                                                                                            |
|----------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [LoginConfig](./src/main/java/com/lamp/lantern/plugins/core/login/config/LoginConfig.java)                           | 登录系统总配置, 最终在Ex粗特中使用的一项<br/>包括系统名(作为Redis前缀), 登录项配置`authChannelConfig`,handlerConfig列表                                                                         |
| AuthChannelConfigList                                                                                                | 一个储存了AuthChannelConfig的列表, 登录系统会根据`AuthChannelConfigList`登录方式                                                                                                 |
| [AuthChannelConfig](../lantern-plugins-api/src/main/java/com/lamp/lantern/plugins/api/config/AuthChannelConfig.java) | Auth配置, 用来生成AuthService, 支持多种登录方式                                                                                                                             |
| HandlerConfigList                                                                                                    | 一个储存了HandlerConfig的列表, 登录系统会根据`HandlerConfigList`配置启用哪些Handler                                                                                                |
| HandlerConfig                                                                                                        | 每个Handler都有自己的配置项, 而配置项会在[AbstractAuthHandler](././src/main/java/com/lamp/lantern/plugins/core/login/AbstractAuthHandler.java)中以泛型参数传入, 所以说Config和Handler类型是绑定的 |



