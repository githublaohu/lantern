package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.servlet.LanternServlet;
import com.lamp.lantern.plugins.core.servlet.SpringMVCServlet;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelExtensionsKt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HandlerExecuteTest {

    private HandlerExecute handlerExecute = new HandlerExecute();
    private LoginConfig loginConfig = new LoginConfig();

    @Before
    public void before() {

        handlerExecute.setAuthService(new TestService());

        //设置loginConfig
        loginConfig.setSystemName("testSystem");
        List<HandlerConfig> handlerConfigList = new java.util.ArrayList<>();
        HandlerConfig handlerConfig = new HandlerConfig();
        handlerConfig.setHandlerName("TestAuthHandler");
        Map<String, String> configMap = new HashMap();
        configMap.put("serviceAddress", "111");
        configMap.put("topic", "topic");
        handlerConfig.setConfigMap(configMap);
        handlerConfig.setRedisName("testRedis");
        handlerConfigList.add(handlerConfig);
        loginConfig.setHandlerConfigList(handlerConfigList);
        loginConfig.setAuthChannelCofing(null);
        Map<String, StatefulRedisConnection<String, String>> connectionClientCache = new HashMap<>();

        handlerExecute.setLoginConfig(loginConfig);

        //设置HandlerList
        List<AuthHandler> handlerList = new java.util.ArrayList<>();
        handlerList.add(new TestAuthHandler());
        handlerExecute.setHandlerList(handlerList);

        //设置servlet
        LanternServlet servlet = Mockito.mock(LanternServlet.class);
        Mockito.when(servlet.getRequest()).thenReturn(new MockHttpServletRequest());
        Mockito.when(servlet.getResponse()).thenReturn(new MockHttpServletResponse());
        handlerExecute.setServlet(servlet);
    }

    @Test
    public void executeTest() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUiName("test");

        //在test中执行了TestService的空行为
        //handler list为空
        try {
            ResultObject resultObject = handlerExecute.execute(userInfo);
            log.info("resultCode:{}", resultObject.getCode());
            log.info("resultMessage:{}", resultObject.getMessage());
        }
        catch (Exception e) {
            log.error("error", e);
            Assert.fail();
        }
    }
}
