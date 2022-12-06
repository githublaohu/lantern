package com.lamp.lantern.plugins.core.login;

import java.util.List;
import java.util.Objects;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerExecute {

	@Setter
	private List<AuthHandler> handlerList;


	@Setter
	private AuthService authService;
	
	@Setter
	private LoginConfig loginConfig;

	public ResultObject<String> execute(UserInfo userInfo) {
		HandlerExecute2 handlerExecute2 = new HandlerExecute2();
		return handlerExecute2.execute();
	}

	class HandlerExecute2 {
		
		private UserInfo userInfo;

		ResultObject<?> resultObject;

		@SuppressWarnings("unchecked")
		public ResultObject<String> execute() {
			try {
				LanternContext.getContext().setAuthService(authService);
				LanternContext.getContext().setLoginConfig(loginConfig);
				this.authBefore();
				if (Objects.nonNull(resultObject)) {
					return ( ResultObject<String>)resultObject;
				}
				AuthResultObject object = authService.auth(userInfo);
				if (Objects.isNull(object.getErrorMessage())) {
					this.userInfo = object.getUserInfo();
				} else {
					log.warn(" auth fail mesage {}", object);
					resultObject = ResultObject.getResultObjectMessgae(3000, object.getErrorMessage());
					this.errer();
				}
				this.authAfter();
				resultObject = ResultObject.getResultObject(200, this.userInfo);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				resultObject =  ResultObject.getResultObjectMessgae(3000, e.getMessage());
			} finally {
				LanternContext.getContext().clear();
			}
			return (ResultObject<String>)resultObject;
		}

		public void authBefore() {
			for (AuthHandler authHandler : handlerList) {
				resultObject = authHandler.authBefore(userInfo);
				if (Objects.nonNull(resultObject)) {
					log.warn("authBefore fail resultObject is {} , userInfo is {}", resultObject , userInfo);
					return;
				}
			}
		}

		public void authAfter() {
			for (AuthHandler authHandler : handlerList) {
				resultObject = authHandler.authAfter(userInfo);
				if (Objects.nonNull(resultObject)) {
					log.warn("authBefore fail resultObject is {} , userInfo is {}", resultObject , userInfo);
					return;
				}
			}
		}

		public void errer() {
			for (AuthHandler authHandler : handlerList) {
				resultObject = authHandler.errer(userInfo);
				if (Objects.nonNull(resultObject)) {
					log.warn("authBefore fail resultObject is {} , userInfo is {}", resultObject , userInfo);
					return;
				}
			}
		}
	}
}
