package com.lamp.lantern.service.action.login.auth;

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
	public Integer registerUserInfoEntity(UserInfo userInfo) {
		return userInfoService.registerUserInfoEntity((UserInfoEntity) userInfo);
	}

	@Override
	public UserInfo checkUser(UserInfo userInfo) {
		UserInfoEntity userInfoEntity = (UserInfoEntity) userInfo;
		UserInfoEntity newUserInfo = userInfoService.checkUserByUserId(userInfoEntity);
		return newUserInfo;
	}

}
