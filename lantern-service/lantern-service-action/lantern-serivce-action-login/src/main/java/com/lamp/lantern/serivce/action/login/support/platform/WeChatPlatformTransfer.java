package com.lamp.lantern.serivce.action.login.support.platform;

public class WeChatPlatformTransfer implements PlatformTransfer {

	@Override
	public void authentication() {
		// TODO Auto-generated method stub
		EmpowerResponse empowerResponse = weChatEmpower.empower(appId, softwareId.getSiSecret(),
	}

	@Override
	public void getUserInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPhone() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getIdentityCard() {
		// TODO Auto-generated method stub

	}

}
