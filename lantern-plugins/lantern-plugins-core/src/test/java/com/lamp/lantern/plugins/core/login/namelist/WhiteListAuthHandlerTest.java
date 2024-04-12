package com.lamp.lantern.plugins.core.login.namelist;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.enums.StatusEnum;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

@Slf4j
public class WhiteListAuthHandlerTest {
    private WhiteListAuthHandler whiteListAuthHandler = new WhiteListAuthHandler();
    private WhiteListConfig whiteListConfig1 = new WhiteListConfig();
    private WhiteListConfig whiteListConfig2 = new WhiteListConfig();
    private WhiteListConfig whiteListConfig3 = new WhiteListConfig();

    @Before
    public void init() {
        whiteListConfig1.setWhiteListSourceType("entity");
        whiteListConfig1.setWhiteListType(WhiteListConfig.WhiteListType.WHITE);

        whiteListConfig2.setWhiteListType(WhiteListConfig.WhiteListType.WHITE);

        whiteListConfig3.setWhiteListType(WhiteListConfig.WhiteListType.BLACK);
    }

    @Test
    public void entityTest(){
        whiteListAuthHandler.setConfig(whiteListConfig1);
        UserInfo userInfo = new UserInfo();
        userInfo.setUiAllowLogin(StatusEnum.ACTIVE);
        ResultObject<String> result = whiteListAuthHandler.authBefore(userInfo);
        Assert.assertNull(result);

        userInfo.setUiAllowLogin(StatusEnum.INACTIVE);
        result = whiteListAuthHandler.authBefore(userInfo);
        Assert.assertNotNull(result);
    }

    @Test
    public void whiteTest(){
        whiteListAuthHandler.setConfig(whiteListConfig2);
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        userInfo.setUiAllowLogin(StatusEnum.ACTIVE);

        StatefulRedisConnection connection = Mockito.mock(StatefulRedisConnection.class);
        Mockito.when(connection.sync()).thenReturn(Mockito.mock(io.lettuce.core.api.sync.RedisCommands.class));
        Mockito.when(connection.sync().hget("", userInfo.getUiId().toString())).thenReturn(userInfo.getUiId().toString());
        whiteListAuthHandler.setConnection(connection);

        ResultObject<String> result = whiteListAuthHandler.authBefore(userInfo);
        Assert.assertNull(result);

        whiteListAuthHandler.setConfig(whiteListConfig3);
        result = whiteListAuthHandler.authBefore(userInfo);
        Assert.assertNotNull(result);
    }
}