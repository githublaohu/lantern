package com.lamp.lantern.plugins.core.login.times;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.core.login.times.LoginTimesAuthHandler.LoginTimesInfo;

import io.lettuce.core.api.StatefulRedisConnection;

public class LoginTimesAuthHandlerTest {

	private LoginTimesAuthHandler loginTimesAuthHandler = new LoginTimesAuthHandler();
	
	@Mock
	private HttpServletRequest httpServletRequest;
	
	
	StatefulRedisConnection<String, String> connection;
	
	@Before
	public void init() {
		loginTimesAuthHandler =  Mockito.spy(new LoginTimesAuthHandler());
		
		Mockito.when(loginTimesAuthHandler.getRequest()).thenReturn(httpServletRequest);
		
		Mockito.when(httpServletRequest.getRemoteAddr()).thenReturn("182.168.0.1");
		
		LoginTimesConfig loginTimesConfig = new LoginTimesConfig();
		loginTimesAuthHandler.setConfig(loginTimesConfig);
		loginTimesAuthHandler.setConnection(connection);
		
	}
	
	@Test
	public void TestAuthBefore() {
		UserInfo userInfo = new UserInfo();
		loginTimesAuthHandler.authBefore(userInfo);
		
		
		
		LoginTimesInfo loginTimesInfo = LanternContext.getContext().getValue("loginTimesAuthHandler");
		Assert.assertNotNull(loginTimesInfo);
	}
	
	@Test
	public void TestAuthAfter() {
		
	}
	
	@Test
	public void TestError() {
		
	}
}
