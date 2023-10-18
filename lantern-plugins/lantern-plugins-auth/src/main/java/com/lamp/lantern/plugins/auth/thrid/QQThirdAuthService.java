package com.lamp.lantern.plugins.auth.thrid;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import com.lamp.lantern.plugins.api.service.AbstractThirdAuthService;

@AuthTypeChannel(loginType = LoginType.THIRD,authChannel = "QQ")
public class QQThirdAuthService extends AbstractThirdAuthService {

	
	@Override
	public void doInitialization() {
	}

	@Override
	public AuthResultObject auth(UserInfo userInfo) {
		return null;
	}

	@Override
	public AuthResultObject getUserInfo(UserInfo userInfo) {
		return null;
	}

	@Override
	public RedirectAddress getRedirectAddress() {
		return null;
	}
}
