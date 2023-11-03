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
package com.lamp.lantern.plugins.auth.thrid;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractThirdAuthService;

@AuthTypeChannel(loginType = LoginType.THIRD,authChannel = "Taobao")
public class TaobaoThirdAuthService extends AbstractThirdAuthService {

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
//	      s=WebUtils.doPost(url, props, 30000, 30000);
		return authResultObject;
	}

	@Override
	public AuthResultObject getUserInfo(UserInfo userInfo) {
		AuthResultObject authResultObject = AuthResultObject.create();
		return authResultObject;
	}

	@Override
	public RedirectAddress getRedirectAddress() {
		String url = "https://oauth.taobao.com/authorize?response_type=code&client_id={0}&redirect_uri={1}&view=web";
		url = MessageFormat.format(url, config.getAppId(), config.getRedirectUri());
		return RedirectAddress.create(url);
	}
}
