package com.lamp.lantern.plugins.core.login;

import com.lamp.lantern.plugins.api.config.AuthChannelCofing;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.environment.EnvironmentContext;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerServiceTest {


    private HandlerService handlerService = new HandlerService();
    private UserInfo userInfo = new UserInfo();

    @Before
    public void init(){
        userInfo.setUiId(1L);
        userInfo.setUiName("testUserName");
        userInfo.setUiNickname("testNickName");
        userInfo.setUiIdcard("testIdCard");
        userInfo.setUiPhone("testPhone");
        userInfo.setUiEmail("testEmail");
        userInfo.setUiHeadPortrait("testHeadPortrait");
        userInfo.setUiLackFlag(1);
        userInfo.setUiSex("testSex");
        userInfo.setUiAge(1);

    }
    @Test
    public void createHandlerExecuteTest() throws Exception {
        LoginConfig loginConfig = new LoginConfig();
        loginConfig.setSystemName("testSystem");
        List<HandlerConfig> handlerConfigList = new ArrayList<>();
        HandlerConfig handlerConfig = new HandlerConfig();
        handlerConfig.setHandlerName("BroadcastAuthHandler");
        Map<String, String> configMap = new HashMap();
        configMap.put("serviceAddress", "111");
        configMap.put("topic", "topic");
        handlerConfig.setConfigMap(configMap);
        handlerConfig.setRedisName("testRedis");
        handlerConfigList.add(handlerConfig);
        loginConfig.setHandlerConfigList(handlerConfigList);
        loginConfig.setAuthChannelCofing(null);
        Map<String, StatefulRedisConnection<String, String>> connectionClientCache = new HashMap<>();


        connectionClientCache.put("testSystem", Mockito.mock(StatefulRedisConnection.class));
        FieldUtils.writeField(handlerService, "connectionClientCache", connectionClientCache, true);


        AuthChannelCofing authChannelCofing = new AuthChannelCofing();


        authChannelCofing.setClassName("com.lamp.lantern.plugins.core.login.TestService");//

        authChannelCofing.setAppId("testAppId");
        authChannelCofing.setAccessKey("testAccessKey");
        authChannelCofing.setSecretAccessKey("testSecretAccessKey");
        authChannelCofing.setPrivateKey("testPrivateKey");
        authChannelCofing.setPublicKey("testPublicKey");

        loginConfig.setAuthChannelCofing(authChannelCofing);

//        EnvironmentContext environmentContext = Mockito.mock(EnvironmentContext.class);
//        AbstrackAuthHandler abstrackAuthHandler = Mockito.mock(AbstrackAuthHandler.class);
//        Mockito.when(environmentContext.getBean(Mockito.anyString())).thenReturn(abstrackAuthHandler);
//        Mockito.when(environmentContext.getBean(Class.class)).thenReturn(abstrackAuthHandler);
//        Mockito.when(abstrackAuthHandler.)

        EnvironmentContext environmentContext = Mockito.mock(EnvironmentContext.class);
        AuthResultObject authResultObject = Mockito.mock(AuthResultObject.class);
        Mockito.when(environmentContext.getBean(Mockito.anyString())).thenReturn(authResultObject);
        Mockito.when(environmentContext.getBean(Class.class)).thenReturn(authResultObject);
        Mockito.when(authResultObject.getUserInfo()).thenReturn(userInfo);

        HashMap connectionMap = new HashMap();
        connectionMap.put("testRedis", "redis://localhost:6379");
        handlerService.createConnection(connectionMap);

        HandlerExecute handlerExecute = handlerService.createHandlerExecute(loginConfig, environmentContext);

        //测试getHandlerExecute
        HandlerExecute handlerExecute1 = handlerService.getHandlerExecute(loginConfig.getSystemName());


    }
}
