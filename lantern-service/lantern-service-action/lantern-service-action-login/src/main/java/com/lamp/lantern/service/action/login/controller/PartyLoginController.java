package com.lamp.lantern.service.action.login.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.lamp.lantern.plugins.api.config.AuthChannelConfig;
import com.lamp.lantern.plugins.core.environment.SpringEnvironmentContext;
import com.lamp.lantern.plugins.core.login.config.LanternUserInfoConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.core.login.HandlerExecute;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/partyLogin")
@RestController("partyLoginController")
@Api(tags = { "一方登录" })
public class PartyLoginController implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	private HandlerService handlerService;

	private LoginConfig loginConfig;

	@PostConstruct
	private void init() throws Exception {

		AuthChannelConfig authChannelConfig = new AuthChannelConfig();
		authChannelConfig.setBeanName("lanternAuthOperate");
		List<AuthChannelConfig> authChannelConfigList = new ArrayList<AuthChannelConfig>();
		authChannelConfigList.add(authChannelConfig);

		//TODO 在这里加载Handler的配置
		List<HandlerConfig> handlerConfigList = new ArrayList<HandlerConfig>();
		
		loginConfig = new LoginConfig();
		loginConfig.setSystemName("lanternFirst");
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
		
		handlerService.createConnection(redisMap);
		handlerService.createHandlerExecute(loginConfig, springEvnironmentContext);

	}

	@PostMapping("accountLogin")
	@ApiOperation(value = "账户密码登录")
	public ResultObject<String> accountLogin(@RequestBody UserInfoEntity userInfoEntity) {
		HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
		return handlerExecute.execute(userInfoEntity);
	}

	@PostMapping("sendVerifyCode")
	@ApiOperation(value = "发送手机验证码")
	public ResultObject<String> sendVerifyCode(@RequestBody UserInfoEntity userInfoEntity) {
		HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
		return handlerExecute.execute(userInfoEntity);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
