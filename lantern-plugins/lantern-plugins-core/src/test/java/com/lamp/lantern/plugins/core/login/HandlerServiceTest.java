package com.lamp.lantern.plugins.core.login;

import com.lamp.lantern.plugins.api.config.AuthChannelCofing;
import com.lamp.lantern.plugins.api.enums.StatusEnum;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.environment.EnvironmentContext;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.servlet.LanternServlet;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerServiceTest {


    private HandlerService handlerService = new HandlerService();
    private UserInfo userInfo = new UserInfo();

    @Before
    public void init() {
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
        userInfo.setAllowLogin(StatusEnum.ACTIVE);

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
        Map<String, StatefulRedisConnection<String, String>> connectionClientCache = new HashMap<>();


        connectionClientCache.put("testSystem", Mockito.mock(StatefulRedisConnection.class));
        FieldUtils.writeField(handlerService, "connectionClientCache", connectionClientCache, true);


        AuthChannelCofing authChannelCofing = new AuthChannelCofing();


        authChannelCofing.setClassName("com.lamp.lantern.plugins.core.login.TestService");

        authChannelCofing.setAppId("testAppId");
        authChannelCofing.setAccessKey("testAccessKey");
        authChannelCofing.setSecretAccessKey("testSecretAccessKey");
        authChannelCofing.setPrivateKey("testPrivateKey");
        authChannelCofing.setPublicKey("testPublicKey");

        loginConfig.setAuthChannelCofing(authChannelCofing);

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

    /**
     * 测试所有的Handler, 但是不测试AuthService
     *
     * @throws Exception
     */
    @Test
    public void fullTest() throws Exception {


        LoginConfig loginConfig = new LoginConfig();
        loginConfig.setSystemName("testSystem/Service");

        //设置AuthChannel
        AuthChannelCofing authChannelCofing = new AuthChannelCofing();
        authChannelCofing.setClassName("com.lamp.lantern.plugins.core.login.TestService");//
        loginConfig.setAuthChannelCofing(authChannelCofing);

        //根据HandlerList启动Handler
        List<HandlerConfig> handlerConfigList = new ArrayList<>();

        //设置HandlerConfigList

        //设置一个TokenConfig
        HandlerConfig tokenHandlerConfig = new HandlerConfig();
        tokenHandlerConfig.setHandlerName("CreateTokenAuthHandler");
        tokenHandlerConfig.setRedisName("testTokenRedis");

        //设置CreateTokenConfigMap
        HashMap<String, String> createTokenConfigMap = new HashMap<>();
        createTokenConfigMap.put("tokenName", "testTokenName");
        createTokenConfigMap.put("tokenCreateMode", "UUID");
        createTokenConfigMap.put("dataPosition", "cookie");
        createTokenConfigMap.put("cookieDomain", "testCookieDomain");

        tokenHandlerConfig.setConfigMap(createTokenConfigMap);


        //设置TimesConfig
        HandlerConfig timesHandlerConfig = new HandlerConfig();
        timesHandlerConfig.setHandlerName("LoginTimesAuthHandler");
        timesHandlerConfig.setRedisName("testTimesRedis");

        HashMap<String, String> timesConfigMap = new HashMap<>();
        timesConfigMap.put("times", "3");

        timesHandlerConfig.setConfigMap(timesConfigMap);

        //set ExclusiveConfig
        HandlerConfig exclusiveHandlerConfig = new HandlerConfig();
        exclusiveHandlerConfig.setHandlerName("ExclusiveAuthHandler");
        exclusiveHandlerConfig.setRedisName("testExclusiveRedis");

        HashMap<String, String> exclusiveConfigMap = new HashMap<>();
        exclusiveConfigMap.put("exclusiveMethod", "KICK_SAME");
        exclusiveConfigMap.put("allowNumber", "3");
        exclusiveHandlerConfig.setConfigMap(exclusiveConfigMap);

        //set name list config
        HandlerConfig nameListHandlerConfig = new HandlerConfig();
        nameListHandlerConfig.setHandlerName("WhiteListAuthHandler");
        nameListHandlerConfig.setRedisName("testWhiteListRedis");

        HashMap<String, String> nameListConfigMap = new HashMap<>();
//        nameListConfigMap.put("whiteListSourceType", "entity");
        nameListConfigMap.put("whiteListSourceType", "list");
        nameListConfigMap.put("whiteListType", "BLACK");
        nameListHandlerConfig.setConfigMap(nameListConfigMap);

        //set in HandlerConfigList
        handlerConfigList.add(tokenHandlerConfig);
        handlerConfigList.add(timesHandlerConfig);
        handlerConfigList.add(nameListHandlerConfig);
        handlerConfigList.add(exclusiveHandlerConfig);

        loginConfig.setHandlerConfigList(handlerConfigList);

        //设置RedisMap
        Map<String, String> redisMap = new HashMap<>();
        redisMap.put("testTokenRedis", "redis://localhost:6379/0");
        redisMap.put("testTimesRedis", "redis://localhost:6379/0");
        redisMap.put("testWhiteListRedis", "redis://localhost:6379/0");
        redisMap.put("testExclusiveRedis", "redis://localhost:6379/0");
        handlerService.createConnection(redisMap);


        EnvironmentContext environmentContext = Mockito.mock(EnvironmentContext.class);
        AuthResultObject authResultObject = Mockito.mock(AuthResultObject.class);
        Mockito.when(environmentContext.getBean(Mockito.anyString())).thenReturn(authResultObject);
        Mockito.when(environmentContext.getBean(Class.class)).thenReturn(authResultObject);
        Mockito.when(authResultObject.getUserInfo()).thenReturn(userInfo);

        //set LanternServlet Mock
        Mockito.when(environmentContext.getLanternServlet()).thenReturn(Mockito.mock(LanternServlet.class));
        Mockito.when(environmentContext.getLanternServlet().getRequest()).thenReturn(new MockHttpServletRequest());
        Mockito.when(environmentContext.getLanternServlet().getResponse()).thenReturn(new MockHttpServletResponse());


        HandlerExecute handlerExecute = handlerService.createHandlerExecute(loginConfig, environmentContext);

        handlerExecute.execute(userInfo);

    }
}
