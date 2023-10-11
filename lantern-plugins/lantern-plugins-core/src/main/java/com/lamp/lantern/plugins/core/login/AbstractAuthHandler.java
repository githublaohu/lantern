package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Setter;

public abstract class AbstractAuthHandler<T> implements AuthHandler {

	@Setter
	protected T config;
	
	@Setter
	private boolean errorResult;
	
	@Setter
	protected String systemName;

	@Setter
	protected StatefulRedisConnection<String, String> connection;


	public void init() {

	}

	@Override
	public  ResultObject<String> authBefore(UserInfo userInfo) {
		this.doAuthBefore(userInfo);
		return null;
	}

	@Override
	public  ResultObject<String> authAfter(UserInfo userInfo) {
		this.doAuthAfter(userInfo);
		return null;
	}

	@Override
	public  ResultObject<String> error(UserInfo userInfo) {
		this.doError(userInfo);
		return null;
	}
	
	
	public void doAuthBefore(UserInfo userInfo) {
	}

	public  void doAuthAfter(UserInfo userInfo) {
	}

	public  void doError(UserInfo userInfo) {
	}

}
