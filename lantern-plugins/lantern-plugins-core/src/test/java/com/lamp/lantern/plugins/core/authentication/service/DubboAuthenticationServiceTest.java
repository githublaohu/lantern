package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.auth.config.DubboAuthenticationConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DubboAuthenticationServiceTest {
    private DubboAuthenticationService dubboAuthenticationService = null;
    private AuthenticationData authenticationData = new AuthenticationData();

    @Before
    public void init() {
        DubboAuthenticationConfig dubboAuthenticationConfig = new DubboAuthenticationConfig();
        dubboAuthenticationConfig.setApplicationName("applicationName");
        dubboAuthenticationConfig.setUrl("dubbo://127.0.0.1:20880");
        dubboAuthenticationConfig.setTimeout(5000);
        dubboAuthenticationConfig.setGroup("group");
        dubboAuthenticationConfig.setVersion("version");

        dubboAuthenticationService = new DubboAuthenticationService(dubboAuthenticationConfig);

        authenticationData.setToken("123456");
        authenticationData.setResource("1");
    }

    @Test
    public void getUserInfoTest() {

        AuthenticationServiceResult result = dubboAuthenticationService.getUserInfo(authenticationData);
        Assert.assertNotNull(result);
        UserInfo userInfo = result.getUserInfo();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void authenticationTest() {
        AuthenticationServiceResult result = dubboAuthenticationService.authentication(authenticationData);
        Assert.assertNotNull(result);
        UserInfo userInfo = result.getUserInfo();
        Assert.assertTrue(result.isSuccess());
    }
}
