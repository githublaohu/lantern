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
