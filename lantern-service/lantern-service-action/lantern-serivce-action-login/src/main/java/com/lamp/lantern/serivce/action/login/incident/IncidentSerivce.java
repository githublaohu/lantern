package com.lamp.lantern.serivce.action.login.incident;

import com.lamp.lantern.service.core.service.LoginRecordService;
import com.lamp.lantern.service.core.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;


import com.lamp.lantern.serivce.action.login.incident.LoginIncident.LoginIncidentBuilder;
import com.lamp.lantern.serivce.action.login.incident.TripartiteIncident.TripartiteIncidentBuilder;

@Component
public class IncidentSerivce {

//	@Reference(url = "127.0.0.1:8022")

	@Reference
	private UserInfoService userInfoService;

	@Reference
	private LoginRecordService loginRecordService;




	public LoginIncident createLoginIncident() {
		LoginIncidentBuilder builder = LoginIncident.builder();
		builder.userInfoService(userInfoService);
		builder.loginRecordService(loginRecordService);
		return builder.build();
	}

	public LoginIncidentBuilder createLoginIncidentBuilder() {
		LoginIncidentBuilder builder = LoginIncident.builder();
		builder.userInfoService(userInfoService);
		builder.loginRecordService(loginRecordService);
		return builder;
	}

	public TripartiteIncident createTripartiteIncident() {
		TripartiteIncidentBuilder builder = TripartiteIncident.builder();
		return builder.build();
	}

}
