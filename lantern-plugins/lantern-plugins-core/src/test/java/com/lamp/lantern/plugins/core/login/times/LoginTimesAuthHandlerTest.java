package com.lamp.lantern.plugins.core.login.times;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class LoginTimesAuthHandlerTest {

    private LoginTimesAuthHandler loginTimesAuthHandler = new LoginTimesAuthHandler();


    private StatefulRedisConnection<String, String> connection = RedisClient.create("redis://localhost").connect();

    @Before
    public void init() {
        LanternContext context = LanternContext.getContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        context.setRequest(request);

        LoginTimesConfig loginTimesConfig = new LoginTimesConfig();
        loginTimesAuthHandler.setSystemName("test");
        loginTimesAuthHandler.init();
        loginTimesAuthHandler.setConfig(loginTimesConfig);
        loginTimesAuthHandler.setConnection(connection);


    }

    @Test
    public void TestAuthBefore() {
        UserInfo userInfo = new UserInfo();
        loginTimesAuthHandler.authBefore(userInfo);

    }

    @Test
    public void TestAuthAfter() {
        UserInfo userInfo = new UserInfo();
        loginTimesAuthHandler.authBefore(userInfo);
        loginTimesAuthHandler.authAfter(userInfo);
    }

    @Test
    public void TestError() {
        UserInfo userInfo = new UserInfo();
        loginTimesAuthHandler.error(userInfo);
    }

    @Test
    public void TestLoginFail() {
        UserInfo userInfo = new UserInfo();
        loginTimesAuthHandler.authBefore(userInfo);
        loginTimesAuthHandler.error(userInfo);
        loginTimesAuthHandler.authBefore(userInfo);
        loginTimesAuthHandler.error(userInfo);
        loginTimesAuthHandler.authBefore(userInfo);
        loginTimesAuthHandler.error(userInfo);
        loginTimesAuthHandler.authBefore(userInfo);
        loginTimesAuthHandler.error(userInfo);
        loginTimesAuthHandler.authBefore(userInfo);
        loginTimesAuthHandler.error(userInfo);
        loginTimesAuthHandler.authBefore(userInfo);
        loginTimesAuthHandler.error(userInfo);
        Assert.assertEquals("5", connection.sync().get("test-user-timenull"));
        loginTimesAuthHandler.authAfter(userInfo);
        Assert.assertNull(connection.sync().get("test-user-timenull"));
    }
}
