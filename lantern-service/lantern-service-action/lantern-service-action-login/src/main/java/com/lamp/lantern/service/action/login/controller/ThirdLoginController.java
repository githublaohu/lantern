package com.lamp.lantern.service.action.login.controller;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.config.AuthChannelConfig;
import com.lamp.lantern.plugins.core.environment.EnvironmentContext;
import com.lamp.lantern.plugins.core.environment.SpringEnvironmentContext;
import com.lamp.lantern.plugins.core.login.HandlerExecute;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LanternUserInfoConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/thirdLogin")
@RestController("thirdLoginController")
@Api(tags = {"三方登录"})
public class ThirdLoginController {
    private ApplicationContext applicationContext;

    private HandlerService handlerService;

    private LoginConfig loginConfig;

    @PostConstruct
    private void init() throws Exception {

        AuthChannelConfig authChannelConfig = new AuthChannelConfig();

        //TODO 在这里加载三方登录的配置
        authChannelConfig.setSimpleClassName("AlipayPlatformAuthService");
        authChannelConfig.setAppId("testAppId");
        authChannelConfig.setAccessKey("testAccessKey");
        authChannelConfig.setSecretAccessKey("testSecretAccessKey");
        authChannelConfig.setPrivateKey("testPrivateKey");
        authChannelConfig.setPublicKey("testPublicKey");

        List<AuthChannelConfig> authChannelConfigList = new ArrayList<>();
        authChannelConfigList.add(authChannelConfig);
        //TODO 在这里加载Handler的配置
        List<HandlerConfig> handlerConfigList = new ArrayList<HandlerConfig>();


        loginConfig = new LoginConfig();
        loginConfig.setSystemName("lanternThird");
        loginConfig.setAuthChannelConfigList(authChannelConfigList);
        loginConfig.setHandlerConfigList(handlerConfigList);
        SpringEnvironmentContext springEvnironmentContext = new SpringEnvironmentContext(applicationContext);

        LanternUserInfoConfig lanternUserInfoConfig = new LanternUserInfoConfig();
        lanternUserInfoConfig.setBeanName("loginUserInfoService");
        loginConfig.setLanternUserInfoConfig(lanternUserInfoConfig);



        handlerService = new HandlerService();
        handlerService.setEnvironmentContext(springEvnironmentContext);

        //设置RedisMap
        Map<String, String> redisMap = new HashMap<>();
        redisMap.put("testTokenRedis", "redis://localhost:6379/0");
        redisMap.put("testTimesRedis", "redis://localhost:6379/0");
        redisMap.put("testWhiteListRedis", "redis://localhost:6379/0");
        redisMap.put("testExclusiveRedis", "redis://localhost:6379/0");
        handlerService.createConnection(redisMap);

        handlerService.createHandlerExecute(loginConfig, springEvnironmentContext);

    }

    @ApiOperation(value = "获取第三方登录跳转uri")
    @PostMapping("oauthUrl")
    public RedirectView getRedirectUrl(@RequestBody String authChannel) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        String redirectUrl = handlerExecute.getRedirectUrl(authChannel);
        return new RedirectView(redirectUrl);
    }

    @PostMapping("redirect")
    @ApiOperation(value = "第三方登录重定向")
    public ResultObject<String> accountLogin(@RequestBody UserInfoEntity userInfoEntity) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        ResultObject<String> result = handlerExecute.execute(userInfoEntity);
        if (result.getCode() == 200) {
            return result;
        } else {
            return result;
        }
    }
}




