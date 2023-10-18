package com.lamp.lantern.plugins.api.service;

import com.lamp.lantern.plugins.api.config.AuthChannelConfig;

public abstract class AbstractAuthService implements AuthService {

	protected AuthChannelConfig config;

	@Override
	public boolean isSameChannel(String authChannel) {
		return config.getAuthChannel().equals(authChannel);
	}
	
	@Override
	public void initialization(AuthChannelConfig config) throws Exception {
		this.config = config;
		this.doInitialization();
	}
	
	public void doInitialization() throws Exception {}
}
