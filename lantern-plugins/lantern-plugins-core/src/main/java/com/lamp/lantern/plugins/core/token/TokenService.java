package com.lamp.lantern.plugins.core.token;

import com.lamp.lantern.plugins.core.token.create.EncryptionTokenService;
import com.lamp.lantern.plugins.core.token.create.RandomTokenService;
import com.lamp.lantern.plugins.core.token.create.UUidTokenService;

public class TokenService {
	
	public TokenService() {
		
	}
	
	public static TokenCreateService createTokenService(TokenConfig tokenConfig) {
		if(tokenConfig.getTokenCreateMode() == TokenCreateMode.UUID) {
			return new UUidTokenService();
		}else if(tokenConfig.getTokenCreateMode() == TokenCreateMode.RANDOMSTRING) {
			return new RandomTokenService();
		}else if(tokenConfig.getTokenCreateMode() == TokenCreateMode.ENCRYPTION) {
			EncryptionTokenService tokenCreateService = new EncryptionTokenService();
			tokenCreateService.config(tokenConfig);
			return tokenCreateService;
		}
		return new UUidTokenService();
		
	}
}
