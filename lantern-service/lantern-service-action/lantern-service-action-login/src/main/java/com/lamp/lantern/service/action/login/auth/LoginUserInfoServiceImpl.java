package com.lamp.lantern.service.action.login.auth;

import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.service.core.entity.PlatformUserInfoEntity;
import com.lamp.lantern.service.core.service.PlatformUserInfoService;
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
	@Reference
	private PlatformUserInfoService platformUserInfoService;

	@Override
	public UserInfo registerThirdLoginUser(UserInfo userInfo, PlatformUserInfo platformUserInfo) {
		userInfo = userInfoService.registerThirdLoginUser((UserInfoEntity) userInfo);
		platformUserInfo.setPuiUserId(userInfo.getUiId());

		//没找到转换失败的原因
		PlatformUserInfoEntity platformUserInfoEntity = new PlatformUserInfoEntity();
		platformUserInfoEntity.setPuiAuthchannel(platformUserInfo.getPuiAuthchannel());
		platformUserInfoEntity.setPuiType(platformUserInfo.getPuiType());
		platformUserInfoEntity.setPuiUnionId(platformUserInfo.getPuiUnionId());
		platformUserInfoEntity.setPuiOpenId(platformUserInfo.getPuiOpenId());
		platformUserInfoEntity.setPuiUserId(userInfo.getUiId());
		platformUserInfoService.registerPlatformUserInfo(platformUserInfoEntity);
		return userInfo;
	}

	@Override
	public UserInfo checkUserByUserId(UserInfo userInfo) {
		return userInfoService.checkUserByUserId((UserInfoEntity) userInfo);
	}

	@Override
	public UserInfo checkUserByUserIdAndTriId(UserInfo userInfo, PlatformUserInfo platformUserInfo) {
		PlatformUserInfoEntity platformUserInfoEntity = new PlatformUserInfoEntity();
		platformUserInfoEntity.setPuiUserId(userInfo.getUiId());
platformUserInfoEntity.setPuiOpenId(platformUserInfo.getPuiOpenId());

		return platformUserInfoService.checkUserByUserIdAndTriId(platformUserInfoEntity);
	}

	@Override
	public UserInfo checkUserByTriIdAndAuthchannel(PlatformUserInfo platformUserInfo) {
		PlatformUserInfoEntity platformUserInfoEntity = new PlatformUserInfoEntity();
		platformUserInfoEntity.setPuiAuthchannel(platformUserInfo.getPuiAuthchannel());
		platformUserInfoEntity.setPuiOpenId(platformUserInfo.getPuiOpenId());
		return platformUserInfoService.checkUserByTriIdAndAuthchannel(platformUserInfoEntity);
	}


}
