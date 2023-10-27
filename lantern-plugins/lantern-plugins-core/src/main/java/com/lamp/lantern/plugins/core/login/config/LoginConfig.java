package com.lamp.lantern.plugins.core.login.config;

import java.util.List;

import com.lamp.lantern.plugins.api.config.AuthChannelConfig;

import lombok.Data;

@Data
public class LoginConfig {
	
	/**
	 * 系统名, 在初始化时会被加载到所有的Handler中, 而且作为Redis的前缀
	 */
	private String systemName;

	private List<HandlerConfig> handlerConfigList;
	
	private List<AuthChannelConfig> authChannelConfigList;

	private LanternUserInfoConfig lanternUserInfoConfig;
}
