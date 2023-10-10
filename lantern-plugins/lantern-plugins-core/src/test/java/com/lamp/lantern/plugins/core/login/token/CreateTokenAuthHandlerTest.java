package com.lamp.lantern.plugins.core.login.token;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.plugins.core.token.TokenCreateMode;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;
import org.junit.Before;
import org.junit.Test;


import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;


public class CreateTokenAuthHandlerTest {

    private CreateTokenAuthHandler createTokenAuthHandler = new CreateTokenAuthHandler();

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setTokenCreateMode(TokenCreateMode.UUID);
        tokenConfig.setTokenName("tokenName");
        Field field = createTokenAuthHandler.getClass().getDeclaredField("tokenConfig");
        field.setAccessible(true);
        field.set(createTokenAuthHandler, tokenConfig);
        StatefulRedisConnection connection = Mockito.mock(StatefulRedisConnection.class);
        Mockito.when(connection.sync()).thenReturn(Mockito.mock(io.lettuce.core.api.sync.RedisCommands.class));
        Mockito.when(connection.sync().set(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn("OK");
        createTokenAuthHandler.setConnection(connection);
        createTokenAuthHandler.init();
        createTokenAuthHandler.setConfig(new TokenAndSessionConfig());
    }

    @Test
    public void tokenAuthAfter() {


        LanternContext context = LanternContext.getContext();
        context.setResponse(new MockHttpServletResponse());
        // 创建UserInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);

        StatefulRedisConnection connection = Mockito.mock(StatefulRedisConnection.class);
        Mockito.when(connection.sync()).thenReturn(Mockito.mock(io.lettuce.core.api.sync.RedisCommands.class));
        Mockito.when(connection.sync().set(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn("OK");
        createTokenAuthHandler.setConnection(connection);
        // 调用要测试的方法
        createTokenAuthHandler.doAuthAfter(userInfo);

        // 验证结果
        HttpServletResponse response = context.getResponse();
        Assert.assertEquals(32, response.getHeader("tokenName").length());

    }

}
