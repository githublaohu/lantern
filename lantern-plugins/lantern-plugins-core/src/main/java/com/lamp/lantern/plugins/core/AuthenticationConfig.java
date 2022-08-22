package com.lamp.lantern.plugins.core;

import java.util.Set;

import lombok.Data;


/**
 * 1. 得到用户数据
 * 2. 得到权限数据
 * 3. 
 * @author laohu
 *
 */
@Data
public class AuthenticationConfig {

	private String systemId;

	private String systemName;

	private Set<String> notAuthentication;

	private Set<String> userAuthentication;

	private int authentication;

	private boolean isResourcesAuthentication = false;

	private String redirectData;

	private String tokenSpot;

	private String tokenName;

	private String redirectSpot;

	private String authType;
}
