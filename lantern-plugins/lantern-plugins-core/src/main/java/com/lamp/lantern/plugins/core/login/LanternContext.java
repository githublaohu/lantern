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

import java.util.HashMap;
import java.util.Map;

import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;

import lombok.Getter;
import lombok.Setter;

public class LanternContext {

	private static final ThreadLocal<LanternContext> LOGIN_CONTEXT_LOCAL = new ThreadLocal<LanternContext>() {
		protected LanternContext initialValue() {
			return new LanternContext();
		}
	};

	
	public static final LanternContext getContext() {
		return LOGIN_CONTEXT_LOCAL.get();
	}
	
	private final Map<String, Object> values = new HashMap<String, Object>();
	
	@Getter
	@Setter(lombok.AccessLevel.PROTECTED)
	private AuthService authService;
	
	@Getter
	@Setter(lombok.AccessLevel.PROTECTED)
	private LoginConfig loginConfig;

	public void setValue(String key, Object value) {
		this.values.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String key) {
		return (T) values.get(key);
	}
	
	protected void clear() {
		this.authService = null;
		this.values.clear();
	}
	
}
