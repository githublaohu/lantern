package com.lamp.lantern.plugins.api.service;

import com.lamp.lantern.plugins.api.config.AuthChannelCofing;

public abstract class AbstractAuthService implements AuthService {

	protected AuthChannelCofing config;
	
	@Override
	public void initialization(AuthChannelCofing config) throws Exception {
		this.config = config;
		this.doInitialization();
	}
	
	public void doInitialization() throws Exception {}
}
