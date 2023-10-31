/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Setter;

public abstract class AbstrackAuthHandler<T> implements AuthHandler {

	@Setter
	protected T config;
	
	@Setter
	private boolean errerResult; 
	
	@Setter
	protected String systemName;

	@Setter
	protected StatefulRedisConnection<String, String> connection;

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
