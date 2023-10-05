package com.lamp.lantern.plugins.core.token.create;

import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenCreateService;

public class EncryptionTokenService implements TokenCreateService{

	private TokenConfig tokenConfig;

	@Override
	public void config(TokenConfig tokenConfig) {
		this.tokenConfig = tokenConfig;
	}
	@Override
	public String createToken(TokenConstructData tokenConstructData) {
		//TODO
		return null;
	}

}
