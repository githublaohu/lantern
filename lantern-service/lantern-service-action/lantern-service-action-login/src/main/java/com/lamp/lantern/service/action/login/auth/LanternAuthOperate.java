package com.lamp.lantern.service.action.login.auth;

import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import com.lamp.lantern.plugins.api.service.LanternUserInfoService;

/**
 * 短信验证码登录 是否走缓存
 * 
 * @author laohu
 *
 */
@Component("first-lantern")
public class LanternAuthOperate extends AbstractAuthService {

	private LanternUserInfoService userInfoService;

	@Override
	public AuthResultObject auth(UserInfo userInfo) {
		UserInfo queryUserInfo = userInfoService.checkUser(userInfo);
		AuthResultObject authResultObject = new AuthResultObject();
		if (queryUserInfo == null) {
			authResultObject.setErrorMessage("用户或者密码不正确");
			return authResultObject;
		}
		String uiSalt = queryUserInfo.getUiSalt();
		String uiSaltPassword = DigestUtils.md5Hex(uiSalt + queryUserInfo.getUiPassword());
		if (Objects.equals(queryUserInfo.getUiSaltPassword(), uiSaltPassword)) {
			authResultObject.setUserInfo(userInfo);
		} else {
			authResultObject.setErrorMessage("用户或者密码不正确");
		}
		return authResultObject;
	}

	@Override
	public AuthResultObject getUserInfo(UserInfo userInfo) {
		AuthResultObject authResultObject = new AuthResultObject();
		authResultObject.setUserInfo(userInfo);
		return authResultObject;
	}
}
