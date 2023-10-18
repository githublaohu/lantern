package com.lamp.lantern.plugins.auth.thrid;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import com.lamp.lantern.plugins.api.service.AbstractThirdAuthService;

@AuthTypeChannel(loginType = LoginType.THIRD,authChannel = "Baidu")
public class BaiduThirdAuthService extends AbstractThirdAuthService {

	@Override
	public AuthResultObject auth(UserInfo userInfo) {
		AuthResultObject authResultObject = AuthResultObject.create();
		return authResultObject;
	}

	@Override
	public AuthResultObject getUserInfo(UserInfo userInfo) {
		AuthResultObject authResultObject = AuthResultObject.create();
		return authResultObject;
	}

	@Override
	public void doInitialization() {
		// TODO Auto-generated method stub

	}

	@Override
	public RedirectAddress getRedirectAddress() {
		return null;
	}
}
