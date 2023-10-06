package com.lamp.lantern.plugins.core.login.exclusive;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class ExclusiveAuthHandlerTest {
    private ExclusiveAuthHandler exclusiveAuthHandler = new ExclusiveAuthHandler();
    private ExclusiveConfig exclusiveConfig = new ExclusiveConfig();
    private StatefulRedisConnection<String, String> connection = RedisClient.create("redis://localhost").connect();

    @Before
    public void init() {
        exclusiveConfig.setMethod(ExclusiveConfig.Method.KICK);
        exclusiveAuthHandler.setConfig(exclusiveConfig);
        exclusiveAuthHandler.init();
        exclusiveAuthHandler.setSystemName("test");
        exclusiveAuthHandler.setConnection(connection);

        LanternContext context = LanternContext.getContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Mobile");
        context.setRequest(request);
    }

    @Test
    public void testKick() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        Assert.assertEquals("[{\"deviceType\":\"MOBILE\",\"IP\":\"127.0.0.1\",\"UA\":\"Mobile\",\"deviceStatus\":\"ONLINE\"}]", connection.sync().get("test-1"));
    }

    @Test
    public void testRefuse() {
        exclusiveConfig.setMethod(ExclusiveConfig.Method.REFUSE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        ResultObject<String> res = exclusiveAuthHandler.authBefore(userInfo);
        Assert.assertEquals(10001, res.getCode().intValue());
    }

    @Test
    public void testNone() {
        exclusiveConfig.setMethod(ExclusiveConfig.Method.NONE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        ResultObject<String> res = exclusiveAuthHandler.authBefore(userInfo);
        Assert.assertNull(res);
    }

    @Test
    public void multiDevice() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Windows");
        LanternContext.getContext().setRequest(request);
        ResultObject<String> res = exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        Assert.assertNull(res);
    }
}
