/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.core.login.token;

import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstrackAuthHandler;
import com.lamp.lantern.plugins.core.token.TockenService;
import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenCreateService;

import io.lettuce.core.SetArgs;

public class CteateTockenAuthHandler extends AbstrackAuthHandler<TokenAndSessionConfig> {

	private static final TockenService tockenService = new TockenService();

	private TokenCreateService tockenCreateService;

	private TokenConfig tokenConfig;

	public void init() {
		tockenCreateService = tockenService.createToken(tokenConfig);
	}

	@Override
	public void doAuthAfter(UserInfo userInfo) {
		String userTnfoString = JSON.toJSONString(userInfo);
		SetArgs setArgs = new SetArgs();
		this.connection.sync().set("", userTnfoString,setArgs);
		
		TokenConstructData tokenConstructData = new TokenConstructData();
		String token = tockenCreateService.createToken(tokenConstructData);
		userInfo.setUiToken(token);
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getResponse();
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
