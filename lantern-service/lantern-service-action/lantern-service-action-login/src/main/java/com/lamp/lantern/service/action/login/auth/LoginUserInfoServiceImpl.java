package com.lamp.lantern.service.action.login.auth;

import com.lamp.lantern.plugins.api.config.AuthChannelConfig;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.service.AuthService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.LanternUserInfoService;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.UserInfoService;

@Component("loginUserInfoService")
public class LoginUserInfoServiceImpl implements LanternUserInfoService {

	@Reference
	private UserInfoService userInfoService;

	@Override
	public UserInfo registerUserInfoEntity(UserInfo userInfo) {
		return userInfoService.registerUserInfoEntity((UserInfoEntity) userInfo);
	}

	@Override
	public UserInfo checkUser(UserInfo userInfo) {
		UserInfo newUserInfo = userInfoService.checkUserByUserId((UserInfoEntity) userInfo);
		return newUserInfo;
	}


}
