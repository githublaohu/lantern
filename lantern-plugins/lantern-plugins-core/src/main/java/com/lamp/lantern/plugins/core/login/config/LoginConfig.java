package com.lamp.lantern.plugins.core.login.config;

import java.util.List;

import com.lamp.lantern.plugins.api.config.AuthChannelCofing;

import lombok.Data;

@Data
public class LoginConfig {
	
	/**
	 * 系统名
	 */
	private String systemName;

	private List<HandlerConfig> handlerConfigList;
	
	private AuthChannelCofing authChannelCofing;
}
