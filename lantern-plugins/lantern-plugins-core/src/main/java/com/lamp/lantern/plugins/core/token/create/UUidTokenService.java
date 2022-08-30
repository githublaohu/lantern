package com.lamp.lantern.plugins.core.token.create;

import java.util.UUID;

import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenCreateService;

public class UUidTokenService implements TokenCreateService {

	@Override
	public String createToken(TokenConstructData tockenConstructData) {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
