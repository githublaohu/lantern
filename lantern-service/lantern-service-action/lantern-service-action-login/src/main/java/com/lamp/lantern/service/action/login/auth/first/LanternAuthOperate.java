package com.lamp.lantern.service.action.login.auth.first;

import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.UserInfoService;

/**
 * 短信验证码登录
 * 是否走缓存
 * @author laohu
 *
 */
@Component("first-lantern")
public class LanternAuthOperate extends AbstractFirstAuthOperate {

	private UserInfoService userInfoService;

	@Override
	public ResultObject<Object> auth(UserInfoEntity userInfoEntity) {
		UserInfoEntity queryUserInfoEntity = null;
		if(Objects.nonNull(userInfoEntity.getUiName())&&Objects.nonNull(userInfoEntity.getUiPassword())) {
			queryUserInfoEntity = userInfoService.checkUserExistByUserName(userInfoEntity);
		}else if(Objects.nonNull(userInfoEntity.getUiPhone())&&Objects.nonNull(userInfoEntity.getUiPassword())) {
			queryUserInfoEntity = userInfoService.checkUserExistByPhone(userInfoEntity);
		}else if(Objects.nonNull(userInfoEntity.getUiEmail())&&Objects.nonNull(userInfoEntity.getUiPassword())) {
			queryUserInfoEntity = userInfoService.checkUserExistByEmail(userInfoEntity);
		}
		return this.partyLonin(queryUserInfoEntity);
	}
	
	protected UserInfoEntity getUserInfoEntity(UserInfoEntity userInfoEntity) {
		// 查第三方记录表
		
		// 查用户表
		
		return null;
	}

	private ResultObject<Object> partyLonin(UserInfoEntity userInfoEntity) {
		if (userInfoEntity == null) {
			return  null;
		}
		String uiSalt = userInfoEntity.getUiSalt();
		String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());
		if (Objects.equals(userInfoEntity.getUiSaltPassword(), uiSaltPassword)) {
			this.setLocalUserinfo(userInfoEntity);
			return null;
		}else {
			return null;
		}
	}
}
