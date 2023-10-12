package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractAuthHandler<T> implements AuthHandler {

	/**
	 * 如果要设置config的值，需要在init方法之前调用
	 */
	@Setter
	protected T config;
	
	@Setter
	private boolean errorResult;

	/**
	 * 在Handler创建的时候会被设置为LoginConfig中的systemName
	 */
	@Setter
	protected String SystemName;

	/**
	 *
	 */
	@Setter
	@Getter
	protected String HandlerName;

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
