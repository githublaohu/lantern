/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.core.login;

import com.alibaba.fastjson.JSONObject;
import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.api.service.LanternUserInfoService;
import com.lamp.lantern.plugins.auth.qrcode.QrcodeAuthService;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.servlet.LanternServlet;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
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

    @Setter
    private LanternContext.SessionWorkInfo sessionWorkInfo;

    /**
     * authBefore(Handler) -> auth(AuthService) -> authAfter
     *
     * @param userInfo    用户信息
     * @param loginType   登录方式
     * @param authChannel 登录渠道
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
        Map<String, AuthService> map = authServiceMap.get(LoginType.THIRD);
        AuthService authService = map.get(authChannel);

        return authService.getRedirectAddress().getUrl();
    }

    public QrcodeAuthService.QrInfo getQrcodeId() {
        QrcodeAuthService authService = (QrcodeAuthService) authServiceMap.get(LoginType.QRCODE).get("Web");
        LanternContext context = LanternContext.getContext();
        HttpServletRequest request = servlet.getRequest();
        JSONObject jsonObject = new JSONObject();

        String ip = request.getHeader("x-forwarded-for");
        ip = Objects.isNull(ip) ? request.getRemoteAddr() : ip;
        jsonObject.put("ip", ip);
        jsonObject.put("userAgent", request.getHeader("User-Agent"));

        return authService.getQrcodeId(jsonObject);
    }

    public ResultObject<String> checkQrcode(String code) {
        QrcodeAuthService authService = (QrcodeAuthService) authServiceMap.get(LoginType.QRCODE).get("Web");
        QrcodeAuthService.QrcodeStatus state = authService.checkQrcodeStateByCode(code);
        if (Objects.isNull(state) || Objects.equals(state, QrcodeAuthService.QrcodeStatus.Timeout)) {
            return ResultObject.getResultObjectMessgae(4000, "二维码不存在或已过期");
        }
        if (Objects.equals(state, QrcodeAuthService.QrcodeStatus.NotScanned)) {
            return ResultObject.getResultObjectMessgae(4001, "二维码未扫描");
        }
        if (Objects.equals(state, QrcodeAuthService.QrcodeStatus.Scanned)) {
            return ResultObject.getResultObjectMessgae(4002, "二维码已扫描");
        }
        if (Objects.equals(state, QrcodeAuthService.QrcodeStatus.Deny)) {
            return ResultObject.getResultObjectMessgae(4005, "二维码已拒绝");
        }

        //二维码已确认，进入登录逻辑
        UserInfo userInfo = new UserInfo();
        userInfo.setUiToken(authService.getUserToken(code));
        return execute(userInfo, LoginType.QRCODE, "Web");
    }

    /**
     * 手机端操作二维码
     *
     * @param operate   操作类型，扫码，确认登录，取消登录
     * @param encryptId 加密后的二维码id
     * @param token     扫描二维码时提交手机登录信息token，确认登录时，提交服务器签发的临时token
     * @return
     */
    public ResultObject<String> qrcodeMobileOperate(QrcodeAuthService.MobileOperate operate, String encryptId, String token) {
        QrcodeAuthService authService = (QrcodeAuthService) authServiceMap.get(LoginType.QRCODE).get("Web");
        QrcodeAuthService.QrcodeStatus state = authService.checkQrcodeState(encryptId);
        if (Objects.isNull(state) || Objects.equals(state, QrcodeAuthService.QrcodeStatus.Timeout)) {
            return ResultObject.getResultObjectMessgae(4000, "二维码不存在或已过期");
        }
        //TODO 在这里加手机电脑异地判断

        if (operate == QrcodeAuthService.MobileOperate.Scan) {
            if (state != QrcodeAuthService.QrcodeStatus.NotScanned) {
                return ResultObject.getResultObjectMessgae(4003, "无效二维码");
            }
            HttpServletRequest request = servlet.getRequest();
            JSONObject mobileInfo = new JSONObject();
            String ip = request.getHeader("x-forwarded-for");
            ip = Objects.isNull(ip) ? request.getRemoteAddr() : ip;
            mobileInfo.put("ip", ip);
            mobileInfo.put("userAgent", request.getHeader("User-Agent"));
            mobileInfo.put("token", token);

            return authService.scan(encryptId, mobileInfo);

        }

        else if (operate == QrcodeAuthService.MobileOperate.Deny) {
            if (state != QrcodeAuthService.QrcodeStatus.Scanned) {
                return ResultObject.getResultObjectMessgae(4003, "无效二维码");
            }
            return authService.deny(encryptId, token);
        }

        else if (operate == QrcodeAuthService.MobileOperate.Auth) {
            if (state != QrcodeAuthService.QrcodeStatus.Scanned) {
                return ResultObject.getResultObjectMessgae(4003, "无效二维码");
            }
            return authService.mobileAuth(encryptId, token);
        }
        return ResultObject.getResultObjectMessgae(4008, "无效操作");
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
                //this.userInfo是前端传入的
                AuthResultObject object = authService.auth(userInfo);

                //一方 + 三方 登录逻辑
                if (Objects.nonNull(object) && Objects.isNull(object.getErrorMessage()) ) {
                    //First登录成功，获取用户信息
                    //auth传入的UserInfo,如果一方登录成功，获取了所有信息；如果是第三方登录，只设置了（token）
                    userInfo = object.getUserInfo();
                    //if third login
                    if (Objects.isNull(userInfo.getUiName())&& loginType == LoginType.THIRD) {
                        //第三方登录的auth无法获取UserInfo，只设置了（token）
                        //第三方登录成功，获取用户信息
                        AuthResultObject authResultObject = authService.getUserInfo(this.userInfo);
                        this.userInfo = authResultObject.getUserInfo();
                        PlatformUserInfo platformUserInfo = authResultObject.getPlatformUserInfo();

                        UserInfo checkUserInfo = lanternUserInfoService.checkUserByTriIdAndAuthchannel(platformUserInfo);

                        if (Objects.isNull(checkUserInfo)) {
                            //用户不存在，注册用户

                            this.userInfo = lanternUserInfoService.registerThirdLoginUser(this.userInfo, platformUserInfo);
                        }
                        else {
                            userInfo = checkUserInfo;
                        }
                    }
                }
                //QRCode登录逻辑
                else if (Objects.isNull(object) && loginType == LoginType.QRCODE) {
                    //二维码登录成功，获取用户信息
                    QrcodeAuthService qrcodeAuthService = (QrcodeAuthService) authService;
                    AuthResultObject authResultObject =  qrcodeAuthService.getUserInfo(userInfo, sessionWorkInfo.getConnection(),sessionWorkInfo.getTokenHandlerName(),sessionWorkInfo.getSystemName() );
                    this.userInfo = authResultObject.getUserInfo();
                }

                else {
                    log.warn(" auth fail message {}", object);
                    //触发NullPointer异常，进入登录失败逻辑
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
