package com.lamp.lantern.plugins.auth.thrid;

import java.util.HashMap;
import java.util.Map;

import com.alipay.api.internal.util.WebUtils;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;

public class TaobaoThridAuthService extends AbstractAuthService {

	@Override
	public AuthResultObject auth(UserInfo userInfo) {
		AuthResultObject authResultObject = AuthResultObject.create();
		String url="https://oauth.taobao.com/token";
	      Map<String,String> props=new HashMap<String,String>();
	      props.put("grant_type","authorization_code");
	      props.put("code","test");
	      props.put("client_id","test");
	      props.put("client_secret","test");
	      props.put("redirect_uri","http://www.test.com");
	      props.put("view","web");
	      String s="";
	      s=WebUtils.doPost(url, props, 30000, 30000);
		return authResultObject;
	}

	@Override
	public AuthResultObject getUserInfo(UserInfo userInfo) {
		AuthResultObject authResultObject = AuthResultObject.create();
		return authResultObject;
	}
}
