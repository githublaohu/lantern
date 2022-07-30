package com.lamp.lantern.plugins.core;

import java.util.Set;

import lombok.Data;


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
