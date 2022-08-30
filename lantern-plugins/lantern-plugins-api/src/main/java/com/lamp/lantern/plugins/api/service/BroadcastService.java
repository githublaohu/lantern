package com.lamp.lantern.plugins.api.service;

import com.lamp.lantern.plugins.api.mode.UserInfo;

public interface BroadcastService {

	public void loginBroadcast(UserInfo userInfo);
	
	public void createUserBroadcast(UserInfo userInfo);
}
