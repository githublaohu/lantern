package com.lamp.lantern.service.action.login.config;

import java.util.Map;

import lombok.Data;

@Data
public class HandlerConfig {

	private String name;
	
	private String redisName;
	
	private Map<String,Object>  config;
}
