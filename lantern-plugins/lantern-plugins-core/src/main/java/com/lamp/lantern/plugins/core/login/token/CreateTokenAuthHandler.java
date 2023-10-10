package com.lamp.lantern.plugins.core.login.token;

import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.LanternContext;

import com.alibaba.fastjson.JSON;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.token.TokenService;
import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenCreateService;

import io.lettuce.core.SetArgs;

public class CreateTokenAuthHandler extends AbstractAuthHandler<TokenAndSessionConfig> {


	private TokenCreateService tokenCreateService;

	private TokenConfig tokenConfig;

	@Override
	public void init() {
		tokenCreateService = TokenService.createTokenService(tokenConfig);
	}

	@Override
	public void doAuthAfter(UserInfo userInfo) {
		TokenConstructData tokenConstructData = new TokenConstructData();
		String token = tokenCreateService.createToken(tokenConstructData);
		String UA = LanternContext.getContext().getRequest().getHeader("User-Agent");
		connection.sync().hset(userInfo.getUiId().toString(),UA, token);

		HttpServletResponse response = LanternContext.getContext().getResponse();
		if (Objects.equals("cookie", super.config.getDataPosition())) {
			Cookie cookie = new Cookie(tokenConfig.getTokenName(), token);
			cookie.setDomain(config.getCookieDomain());
			cookie.setMaxAge(config.getCookieMaxAge());
			cookie.setSecure(config.isCookieSecure());
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
		} else {
			response.addHeader(tokenConfig.getTokenName(), token);
		}
	}
}
