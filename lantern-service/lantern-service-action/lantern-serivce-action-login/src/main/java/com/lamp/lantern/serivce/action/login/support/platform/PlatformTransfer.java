package com.lamp.lantern.serivce.action.login.support.platform;

public interface PlatformTransfer {

	// 平台的接口类 具体平台实现继承该类

	public void authentication();

	public void getUserInfo();

	public void getPhone();

	public void identityCard();
}
