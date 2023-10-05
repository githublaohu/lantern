package com.lamp.lantern.plugins.core.login;

import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import org.mockito.Mockito;

public class TestService extends AbstractAuthService {


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
