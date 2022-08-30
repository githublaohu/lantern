package com.lamp.lantern.plugins.core.login.broadcast;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.BroadcastService;
import com.lamp.lantern.plugins.core.login.AbstrackAuthHandler;

public class BroadcastAuthHandler extends AbstrackAuthHandler<BroadcastConfig> {

	private BroadcastService broadcastService;
	
	@Override
	public void doAuthAfter(UserInfo userInfo) {
		broadcastService.loginBroadcast(userInfo);
	}
}
