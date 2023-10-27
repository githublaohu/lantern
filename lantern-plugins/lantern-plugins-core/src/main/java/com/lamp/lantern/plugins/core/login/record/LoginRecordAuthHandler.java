package com.lamp.lantern.plugins.core.login.record;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.database.LoginRecord;
import com.lamp.lantern.service.core.entity.enums.LoginStatusEnum;
import com.lamp.lantern.service.core.service.LoginRecordService;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;

import java.sql.Date;
import java.time.LocalDate;
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
