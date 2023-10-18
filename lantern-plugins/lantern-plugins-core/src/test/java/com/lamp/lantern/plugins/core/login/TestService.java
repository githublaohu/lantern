package com.lamp.lantern.plugins.core.login;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import org.mockito.Mockito;

@AuthTypeChannel(loginType = LoginType.PLATFORM, authChannel = "TEST")
public class TestService extends AbstractAuthService {


    /**
     * 默认登录成功, 用于测试
     * @param userInfo
     * @return
     */
    @Override
    public AuthResultObject auth(UserInfo userInfo) {
        AuthResultObject result = new AuthResultObject();
        result.setUserInfo(userInfo);
        return result;
    }

    @Override
    public AuthResultObject getUserInfo(UserInfo userInfo) {
        AuthResultObject result = new AuthResultObject();
        result.setUserInfo(userInfo);
        return result;
    }
}
