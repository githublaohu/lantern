# 什么是lantern？
lantern是一个提供多种登录方式和登录相关功能的用户管理系统。lantern可以通过自定义配置来开启不同的登录方式和登录相关功能。

## 支持的登录方式
### 一方登录
一方登录是最常见的登录方式，用户使用用户名+密码的方式登录系统，同时邮箱、手机号等唯一识别用户的信息也可以作为用户名使用，事实发送的验证码也可以代替密码的位置。
### 二方登录
有时项目已有一套登录系统，lantern也是支持接入的，用户在原项目中登录后获取token，然后lantern使用token从原有系统中代替用户登录。
### 三方登录
三方登录是指用户使用第三方账号登录系统，如微信、QQ、微博等。
### 二维码登录
lantern支持手机端登录后，使用二维码的方式登录PC端。

我们可以发现在上面所描述的四种登录方式中都需要一些通用的功能，比如登录注销，登录时间记录等，lantern为了避免了重复开发，提高代码复用率，将这些功能封装起来，提供一个通用的接口。开发者可以通过实现接口扩展自定义的登录方式，比如只要实现了`AuthService`就可以通过配置自定义新的第三方登录方式。

## 支持的登录相关功能
lantern使用`AuthHandler`提供登录衍生的功能。`AuthHandler`能够处理登录之前，登录成功后和登录失败三种状态。
### 目前已有的`AuthHandler`
#### [CreateTokenAuthHandler](..%2Flantern-plugins%2Flantern-plugins-core%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fcore%2Flogin%2Ftoken%2FCreateTokenAuthHandler.java)
默认开启的`AuthHandler`，用来创建token并返回给前端，同时将token存储到redis中，记录用户的登录状态。
#### [BroadcastAuthHandler](..%2Flantern-plugins%2Flantern-plugins-core%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fcore%2Flogin%2Fbroadcast%2FBroadcastAuthHandler.java)
用来广播登录成功和登录失败的`AuthHandler`。
#### [ExclusiveAuthHandler](..%2Flantern-plugins%2Flantern-plugins-core%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fcore%2Flogin%2Fexclusive%2FExclusiveAuthHandler.java)
用来限制同一个用户能否同时在多个设备上登录lantern系统，可以配置按数量限制、按设备类型限制等多种方案。
#### [WhiteListAuthHandler](..%2Flantern-plugins%2Flantern-plugins-core%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fcore%2Flogin%2Fnamelist%2FWhiteListAuthHandler.java)
黑白名单设置，限制指定id用户能否登录。
#### [LoginRecordAuthHandler](..%2Flantern-plugins%2Flantern-plugins-core%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fcore%2Flogin%2Frecord%2FLoginRecordAuthHandler.java)
能够记录用户的登录时间，登录ip，登录设备等信息。
#### [LoginTimesAuthHandler](..%2Flantern-plugins%2Flantern-plugins-core%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fcore%2Flogin%2Ftimes%2FLoginTimesAuthHandler.java)
用来记录同一用户登录失败的次数，如果登录失败次数过多，可以限制用户在一定时间内不能登录。
#### [VerificationHandler](..%2Flantern-plugins%2Flantern-plugins-core%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fcore%2Flogin%2Fverification%2FVerificationHandler.java)
TODO：验证码

## 资源权限认证
lantern支持基于角色的访问控制(RBAC)，提供三种不同的资源权限规则：
1. 无需认证：公共权限，默认即可访问
2. 基于用户：登录后即可访问 // TODO存疑？没搞懂
3. 基于资源：登录并且拥有特定资源权限后才可以访问

## 记录操作用户
lantern能够记录各种操作（比如插入一条资源）的操作人，这是通过向[OperateInfoInjection](..%2Flantern-plugins%2Flantern-plugins-api%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fapi%2Finjection%2FOperateInfoInjection.java)对象的`operatorId`属性打上[@UserInjection](..%2Flantern-plugins%2Flantern-plugins-api%2Fsrc%2Fmain%2Fjava%2Fcom%2Flamp%2Flantern%2Fplugins%2Fapi%2Fannotation%2FUserInjection.java)实现的。`operatorId`对应数据库字段的操作者，而`@UserInjection`注解会从`LanternContext`中获取当前登录用户的id，将其视为操作者，并注入到`operatorId`中。

# 为什么我们要创造lantern？
如今互联网服务大多都涉及用户管理相关的部分，

# 如何启动lantern？

# lantern的配置