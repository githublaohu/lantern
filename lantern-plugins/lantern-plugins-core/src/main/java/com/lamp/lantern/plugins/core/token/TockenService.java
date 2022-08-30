package com.lamp.lantern.plugins.core.token;

import com.lamp.lantern.plugins.core.token.create.EncryptionTokenService;
import com.lamp.lantern.plugins.core.token.create.RandomTokenService;
import com.lamp.lantern.plugins.core.token.create.UUidTokenService;

public class TockenService {
	
	public TockenService() {
		
	}
	
	public TokenCreateService  createToken(TokenConfig tokenConfig) {
		if(tokenConfig.getTockenCreateMode() == TokenCreateMode.UUID) {
			return new UUidTokenService();
		}else if(tokenConfig.getTockenCreateMode() == TokenCreateMode.RANDOMSTRING) {
			return new RandomTokenService();
		}else if(tokenConfig.getTockenCreateMode() == TokenCreateMode.ENCRYPTION) {
			EncryptionTokenService tockenCreateService = new EncryptionTokenService();
			tockenCreateService.config(tokenConfig);
			return tockenCreateService;
		}
		return new UUidTokenService();
		
	}
}
