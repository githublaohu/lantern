package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Setter;

public abstract class AbstrackAuthHandler<T> implements AuthHandler {

	protected T config;
	
	@Setter
	private boolean errerResult; 

	@Setter
	protected StatefulRedisConnection<String, String> connection;

	public void setConfig(T config) {
		this.config = config;
	}

	public void init() {

	}

	public  ResultObject<String> authBefore(UserInfo userInfo) {
		this.doAuthBefore(userInfo);
		return null;
	}

	public  ResultObject<String> authAfter(UserInfo userInfo) {
		this.doAuthAfter(userInfo);
		return null;
	}

	public  ResultObject<String> errer(UserInfo userInfo) {
		this.doErrer(userInfo);
		return null;
	}
	
	
	public void doAuthBefore(UserInfo userInfo) {
	}

	public  void doAuthAfter(UserInfo userInfo) {
	}

	public  void doErrer(UserInfo userInfo) {
	}

}
