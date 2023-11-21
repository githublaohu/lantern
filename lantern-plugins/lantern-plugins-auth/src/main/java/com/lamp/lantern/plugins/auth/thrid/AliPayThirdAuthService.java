package com.lamp.lantern.plugins.auth.thrid;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractThirdAuthService;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.text.MessageFormat;

@Slf4j
@AuthTypeChannel(loginType = LoginType.THIRD, authChannel = "Alipay")
public class AliPayThirdAuthService extends AbstractThirdAuthService {

    private DefaultAlipayClient alipayClient;

    @Override
    public void doInitialization() {
        alipayClient = new DefaultAlipayClient(config.getServiceAddress(), config.getAppId(), config.getPrivateKey(),
                config.getFormat(), config.getCharset(), config.getPublicKey(), config.getSignType());
    }

    @Override
    public AuthResultObject auth(UserInfo userInfo) {
        AuthResultObject authResultObject = AuthResultObject.create();
        try {
            AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
            request.setCode(userInfo.getToken());
            request.setGrantType("authorization_code");
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            if (oauthTokenResponse.isSuccess()) {

            } else {
                authResultObject.setErrorMessage("");
            }
        } catch (Exception e) {
            authResultObject.setErrorMessage("");
            log.error(e.getMessage(), e);
        }
        return authResultObject;
    }

    @Override
    public AuthResultObject getUserInfo(UserInfo userInfo) {
        AuthResultObject authResultObject = AuthResultObject.create();
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        try {
            AlipayUserInfoShareResponse response = alipayClient.execute(request, userInfo.getToken());
            if (response.isSuccess()) {
                UserInfo userInfoEntity = new UserInfo();
                //TODO 插入用户信息
                authResultObject.setUserInfo(userInfoEntity);
            } else {
                authResultObject.setErrorMessage("根据 access_token获取用户信息失败!");
            }
        } catch (Exception e) {
            authResultObject.setErrorMessage("根据 access_token获取用户信息抛出异常！");
        }
        return authResultObject;
    }

    @Override
    public RedirectAddress getRedirectAddress() {
        String url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id={0}&scope={1}&redirect_uri={2}";
        url = MessageFormat.format(url, config.getAppId(),"auth_user", URLEncoder.encode(config.getRedirectUri()));
        return RedirectAddress.create(url);
    }
}
