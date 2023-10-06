package com.lamp.lantern.plugins.core.login.namelist;

import java.util.Objects;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.enums.StatusEnum;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.AuthHandler;

public class WhiteListAuthHandler extends AbstractAuthHandler<WhiteListConfig> implements AuthHandler {

	private ResultObject<String> whiteResultObject = ResultObject.getResultObjectMessgae(3001, "用户不在白名单里面，登录失败");

	private ResultObject<String> blackResultObject = ResultObject.getResultObjectMessgae(3001, "用户在黑名单里面，登录失败");

	@Override
	public ResultObject<String> authBefore(UserInfo userInfo) {
		Boolean isAuth = false;
		if (Objects.equals(config.getWhiteListSourceType(), "entity")) {
			isAuth = userInfo.getAllowLogin() == StatusEnum.ACTIVE;
		} else {
			String value = connection.sync().hget("", userInfo.getUiId().toString());
			if (Objects.equals(config.getWhiteListType(), "white")) {
				isAuth = Objects.nonNull(value);
			} else {
				isAuth = Objects.isNull(value);
			}
		}
		if (!isAuth) {
			return Objects.equals(config.getWhiteListType(), "") ? whiteResultObject : blackResultObject;
		}
		return null;
	}
}
