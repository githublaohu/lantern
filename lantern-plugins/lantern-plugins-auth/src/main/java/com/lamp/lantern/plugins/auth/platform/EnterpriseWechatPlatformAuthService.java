package com.lamp.lantern.plugins.auth.platform;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;

@AuthTypeChannel(loginType = LoginType.PLATFORM,authChannel = "Wechat")
public class EnterpriseWechatPlatformAuthService extends AbstractAuthService{

	@Override
	public AuthResultObject auth(UserInfo userInfo) {
		return null;
	}

	@Override
	public AuthResultObject getUserInfo(UserInfo userInfo) {
		return null;
	}

}
