package com.lamp.lantern.plugins.core.token.create;

import org.apache.commons.lang3.RandomStringUtils;

import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenCreateService;

public class RandomTokenService implements TokenCreateService{

	@Override
	public String createToken(TokenConstructData tockenConstructData) {
		return RandomStringUtils.random(32);
	}

}
