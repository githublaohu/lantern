package com.lamp.lantern.plugins.auth.thrid;

import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;

public class BaiduThridAuthService extends AbstractAuthService {

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

}
