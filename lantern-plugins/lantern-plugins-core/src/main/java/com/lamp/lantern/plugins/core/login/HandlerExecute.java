package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.api.service.LanternUserInfoService;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.servlet.LanternServlet;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用于执行登录
 */
@Slf4j
public class HandlerExecute {

    @Setter
    private List<AuthHandler> handlerList;

    @Setter
    private Map<LoginType, Map<String, AuthService>> authServiceMap;


    @Setter
    private LoginConfig loginConfig;

    @Setter
    private LanternServlet servlet;

    @Setter
    private LanternUserInfoService lanternUserInfoService;

    /**
     * authBefore(Handler) -> auth(AuthService) -> authAfter
     *
     * @param userInfo 用户信息
     *                 loginType 登录方式
     *                 authChannel 登录渠道
     * @return
     */
    public ResultObject<String> execute(UserInfo userInfo, LoginType loginType, String authChannel) {
        HandlerExecute2 handlerExecute2 = new HandlerExecute2();
        handlerExecute2.userInfo = userInfo;
        handlerExecute2.loginType = loginType;
        handlerExecute2.authChannel = authChannel;
        return handlerExecute2.execute();
    }

    public ResultObject<String> execute(UserInfo userInfo) {
        HandlerExecute2 handlerExecute2 = new HandlerExecute2();
        handlerExecute2.userInfo = userInfo;
        handlerExecute2.loginType = LoginType.FIRST;
        handlerExecute2.authChannel = "Lantern";
        return handlerExecute2.execute();
    }
    public String getRedirectUrl(String authChannel) {
        return authServiceMap.get(LoginType.THIRD).get(authChannel).getRedirectAddress().getUrl();
    }


    class HandlerExecute2 {



        private UserInfo userInfo;
        LoginType loginType;
        String authChannel;

        ResultObject<?> resultObject;


        @SuppressWarnings("unchecked")
        public ResultObject<String> execute() {
            try {
                LanternContext context = LanternContext.getContext();
                context.setResponse(servlet.getResponse());
                context.setRequest(servlet.getRequest());

                AuthService authService = authServiceMap.get(loginType).get(authChannel);

                context.setAuthService(authService);
                context.setLoginConfig(loginConfig);
                this.authBefore();
                if (Objects.nonNull(resultObject)) {
                    return (ResultObject<String>) resultObject;
                }
                AuthResultObject object = authService.auth(userInfo);
                if (Objects.isNull(object.getErrorMessage())) {
                    UserInfo userInfo = object.getUserInfo();
                    if (Objects.isNull(userInfo)){
                        userInfo = lanternUserInfoService.checkUser(this.userInfo);
                        if(Objects.isNull(userInfo)){
                            this.userInfo = authService.getUserInfo(this.userInfo).getUserInfo();
                            this.userInfo = lanternUserInfoService.registerUserInfoEntity(this.userInfo);
                        }
                    }
                } else {
                    log.warn(" auth fail message {}", object);
                    resultObject = ResultObject.getResultObjectMessgae(3000, object.getErrorMessage());
                    this.error();
                }
                this.authAfter();
                resultObject = ResultObject.getResultObject(200, this.userInfo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultObject = ResultObject.getResultObjectMessgae(3000, e.getMessage());
            } finally {
                LanternContext.getContext().clear();
            }
            return (ResultObject<String>) resultObject;
        }

        public void authBefore() {
            for (AuthHandler authHandler : handlerList) {
                resultObject = authHandler.authBefore(userInfo);
                if (Objects.nonNull(resultObject)) {
                    log.warn("authBefore fail resultObject is {} , userInfo is {}", resultObject, userInfo);
                    return;
                }
            }
        }

        public void authAfter() {
            for (AuthHandler authHandler : handlerList) {
                resultObject = authHandler.authAfter(userInfo);
                if (Objects.nonNull(resultObject)) {
                    log.warn("authBefore fail resultObject is {} , userInfo is {}", resultObject, userInfo);
                    return;
                }
            }
        }

        public void error() {
            for (AuthHandler authHandler : handlerList) {
                resultObject = authHandler.error(userInfo);
                if (Objects.nonNull(resultObject)) {
                    log.warn("authBefore fail resultObject is {} , userInfo is {}", resultObject, userInfo);
                    return;
                }
            }
        }
    }
}
