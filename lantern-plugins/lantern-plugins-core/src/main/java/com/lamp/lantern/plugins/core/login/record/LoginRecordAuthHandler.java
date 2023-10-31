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
package com.lamp.lantern.plugins.core.login.record;

import javax.annotation.Resource;

import com.lamp.lantern.plugins.api.mode.LoginRecordInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.LoginRecordService;
import com.lamp.lantern.plugins.core.login.AbstrackAuthHandler;

public class LoginRecordAuthHandler extends AbstrackAuthHandler<Object> {
	
	@Resource
	private LoginRecordService loginRecordService;
	
	@Override
	public void doAuthAfter(UserInfo userInfo) {
		this.loginRecord("success", userInfo);
	}
	
	public void doErrer(UserInfo userInfo){
		this.loginRecord("fail", userInfo);
	}
	
	private void loginRecord(String type , UserInfo userInfo ) {
		LoginRecordInfo loginRecordInfo = new LoginRecordInfo();
		loginRecordService.insertLoginRecord(loginRecordInfo);
	}
}
