package com.lamp.lantern.service.action.login.handler;

import java.util.List;
import java.util.Objects;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerExecute {
	

	@Setter
	private List<AuthHandler> handlerList;

	@Setter
	private UserInfoEntity userInfoEntity;

	@Setter
	private AuthOperate authOperate;
	
	ResultObject<Object> resultObject;

	public ResultObject<Object> execute() {
		try {
			this.authBefore();
			if(Objects.nonNull(resultObject)) {
				return resultObject;
			}
			resultObject = authOperate.auth(userInfoEntity);
			if(resultObject.getData() instanceof UserInfoEntity) {
				this.userInfoEntity = (UserInfoEntity)resultObject.getData();
			}else {
				this.errer();
			}
			this.authAfter();
		}catch(Exception e) {
			log.error(e.getMessage() ,e);
			this.errer();
		}
		return resultObject;
	}

	public void authBefore() {
		for(AuthHandler authHandler : handlerList ) {
			resultObject = authHandler.authBefore(userInfoEntity, authOperate);
			if(Objects.nonNull(resultObject)) {
				return;
			}
		}
	}

	public void authAfter() {
		for(AuthHandler authHandler : handlerList ) {
			resultObject = authHandler.authAfter(userInfoEntity, authOperate);
			if(Objects.nonNull(resultObject)) {
				return;
			}
		}
	}

	public void errer() {
		for(AuthHandler authHandler : handlerList ) {
			resultObject = authHandler.errer(userInfoEntity, authOperate);
			if(Objects.nonNull(resultObject)) {
				return;
			}
		}
	}
}
