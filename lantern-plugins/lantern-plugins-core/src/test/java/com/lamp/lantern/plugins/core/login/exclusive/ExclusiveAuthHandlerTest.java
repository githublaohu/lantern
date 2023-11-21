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
        exclusiveAuthHandler.setConfig(exclusiveConfig);
        exclusiveAuthHandler.init();
        exclusiveAuthHandler.setConnection(connection);

        LanternContext context = LanternContext.getContext();
        LanternContext.SessionWorkInfo sessionWorkInfo = LanternContext.getContext().getSessionWorkInfo();
        //测试中使用了和Exclusive相同的redis连接
        sessionWorkInfo.setConnection(connection);
        sessionWorkInfo.setTokenHandlerName("Session");
        context.setSessionWorkInfo(sessionWorkInfo);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Mobile");
        context.setRequest(request);
        context.setToken("f4770076aa76439ca456225e4964979b");
    }

    @Test
    public void testKickSame() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
    }

    @Test
    public void testKickAll() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Windows");
        LanternContext.getContext().setRequest(request);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);


        exclusiveConfig.setExclusiveMethod(ExclusiveConfig.ExclusiveMethod.KICK_ALL);

        request.setRemoteAddr("192.168.3.1");
        request.addHeader("User-Agent", "Windows");
        LanternContext.getContext().setRequest(request);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        Assert.assertEquals(1, connection.sync().hkeys("Exclusive-1").size());

    }
    @Test
    public void testKickType() {
        exclusiveConfig.setExclusiveMethod(ExclusiveConfig.ExclusiveMethod.KICK_TYPE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.0.0");
        request.addHeader("User-Agent", "Mobile");
        LanternContext.getContext().setRequest(request);

        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);

    }

    @Test
    public void testRefuse() {
        exclusiveConfig.setExclusiveMethod(ExclusiveConfig.ExclusiveMethod.REFUSE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        ResultObject<String> res = exclusiveAuthHandler.authBefore(userInfo);
        Assert.assertEquals(10001, res.getCode().intValue());
    }

    @Test
    public void testAllowN() {
        exclusiveConfig.setAllowNumber(2);
        exclusiveConfig.setExclusiveMethod(ExclusiveConfig.ExclusiveMethod.ALLOW_NUMBER);
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Windows");
        LanternContext.getContext().setRequest(request);
        exclusiveAuthHandler.authBefore(userInfo);
        exclusiveAuthHandler.authAfter(userInfo);
        ResultObject<String> res = exclusiveAuthHandler.authBefore(userInfo);
        Assert.assertEquals(10101, res.getCode().intValue());
    }

    @Test
    public void testNone() {
        exclusiveConfig.setExclusiveMethod(ExclusiveConfig.ExclusiveMethod.NONE);
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
