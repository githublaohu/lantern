package com.lamp.lantern.plugins.api.service;

import com.lamp.lantern.plugins.api.config.AuthChannelConfig;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;

public interface AuthService {
	
	public void initialization(AuthChannelConfig config) throws Exception;

	/**
	 * 定义出参，入参
	 * 
	 * @return
	 */
	public AuthResultObject auth(UserInfo userInfo);
	public boolean isSameChannel(String authChannel);
	/**
	 * 是否需要调用手机号码，身份证
	 * 
	 * @return
	 */
	public AuthResultObject getUserInfo(UserInfo userInfo);
}
