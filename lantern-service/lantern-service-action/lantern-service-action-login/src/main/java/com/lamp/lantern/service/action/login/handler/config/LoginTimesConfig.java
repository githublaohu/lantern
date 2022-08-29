package com.lamp.lantern.service.action.login.handler.config;

import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class LoginTimesConfig {

	private Integer times = -1;
	
	private Integer timeLong = 1;
	
	private TimeUnit timeUnit = TimeUnit.DAYS;
	
	/**
	 * 
	 */
	private Integer addressLimes;
	
	private Integer addressTimeLong = -1;
	
	private TimeUnit addressTimeUnit = TimeUnit.DAYS;
	
	
}
