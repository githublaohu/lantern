package com.lamp.lantern.plugins.core;

import java.util.List;

import lombok.Data;

/**
 * 本地
 * redis
 * MySQL
 * 远程
 * 自定义
 * 
 * 临时，定时加载
 * 
 */
@Data
public class LanternConfig {

	private AuthenticationConfig authenticationConfig;

	private List<AuthenticationConfig> authenticationConfigList;
	
	 
}
