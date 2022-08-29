package com.lamp.lantern.service.action.login.config;

import java.util.List;

import lombok.Data;

@Data
public class LoginConfig {

	private LoginAuthentication authenticationMode;
	
	
	private List<HandlerConfig> handlerConfig;
	
	
	
}
