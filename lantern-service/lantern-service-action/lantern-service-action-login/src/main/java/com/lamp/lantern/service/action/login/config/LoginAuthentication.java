package com.lamp.lantern.service.action.login.config;

import lombok.Data;

@Data
public class LoginAuthentication{

	/**
	 * 一方，
	 * 二方
	 * 三方
	 * 平台
	 */
	private String loginMode;
	
	/**
	 * 厂商
	 */
	private String loginSquare;
	
	private String appId;
	
	private String appkey;
	
	private String appsercet;
}
