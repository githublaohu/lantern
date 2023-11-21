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
import javax.servlet.http.HttpServletRequest;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.plugins.api.enums.LoginStatusEnum;
import com.lamp.lantern.service.core.service.LoginRecordService;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;

import java.time.LocalDateTime;

public class LoginRecordAuthHandler extends AbstractAuthHandler<Object> {
	
	@Resource
	private LoginRecordService loginRecordService;
	
	@Override
	public void doAuthAfter(UserInfo userInfo) {
		this.loginRecord(
				LoginStatusEnum.SUCCESS, userInfo);
	}
	
	@Override
	public void doError(UserInfo userInfo){
		this.loginRecord(LoginStatusEnum.FAIL, userInfo);
	}
	
	private void loginRecord(LoginStatusEnum type , UserInfo userInfo ) {

		HttpServletRequest request = LanternContext.getContext().getRequest();
		String ip = request.getHeader("x-forwarded-for");
		ip = ip == null ? request.getRemoteAddr() : ip;


		LoginRecordEntity loginRecordEntity = new LoginRecordEntity();
		loginRecordEntity.setUiId(userInfo.getUiId());
		loginRecordEntity.setUlLoginTime(LocalDateTime.now());

		loginRecordEntity.setUlLoginIp(ip);

		loginRecordEntity.setUlLoginStatus(type);


		loginRecordService.insertLoginRecord(loginRecordEntity);
	}
}
