package com.lamp.lantern.service.action.login.incident;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import com.lamp.lantern.service.action.login.incident.LoginIncident.LoginIncidentBuilder;
import com.lamp.lantern.service.action.login.incident.TriPartiteIncident.TriPartiteIncidentBuilder;
import com.lamp.lantern.service.action.login.security.JwtTokenService;
import com.lamp.lantern.service.action.login.security.LoginErrorCountService;
import com.lamp.lantern.service.action.login.security.RedisService;
import com.lamp.lantern.service.core.service.LoginRecordService;
import com.lamp.lantern.service.core.service.UserInfoService;

@Component
public class IncidentService {

	// 采用构造者模式

	@Reference
	private UserInfoService userInfoService;

	@Reference
	private LoginRecordService loginRecordService;

	@Reference
	private RedisService redisService;

	@Reference
	private JwtTokenService jwtTokenService;

	@Reference
	private LoginErrorCountService loginErrorCountService;

	public LoginIncident createLoginIncident() {
		LoginIncidentBuilder builder = LoginIncident.builder();
		builder.redisService(redisService).jwtTokenService(jwtTokenService).userInfoService(userInfoService)
				.loginErrorCountService(loginErrorCountService).loginRecordService(loginRecordService);
		return builder.build();
	}

	public LoginIncidentBuilder createLoginIncidentBuilder() {
		LoginIncidentBuilder builder = LoginIncident.builder();
		builder.redisService(redisService).jwtTokenService(jwtTokenService).userInfoService(userInfoService)
				.loginErrorCountService(loginErrorCountService).loginRecordService(loginRecordService);
		return builder;
	}

	public TriPartiteIncident createTriPartiteIncident() {
		TriPartiteIncidentBuilder builder = TriPartiteIncident.builder();

		return builder.build();
	}

	public TriPartiteIncidentBuilder createTriPartiteIncidentBuilder() {
		TriPartiteIncidentBuilder builder = TriPartiteIncident.builder();

		// 根据功能设置属性
		return builder;
	}

}
