package com.lamp.lantern.plugins.core.login.namelist;

import java.util.Objects;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.enums.StatusEnum;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.AuthHandler;

/**
 * 黑白名单Handler
 * 启用名单模式的时候需要在config中设置whiteListSourceType不为"entity",并且需要把名单数据加载到Redis中
 */
public class WhiteListAuthHandler extends AbstractAuthHandler<WhiteListConfig>{

	private ResultObject<String> whiteResultObject = ResultObject.getResultObjectMessgae(3001, "用户不在白名单里面，登录失败");

	private ResultObject<String> blackResultObject = ResultObject.getResultObjectMessgae(3001, "用户在黑名单里面，登录失败");

	@Override
	public ResultObject<String> authBefore(UserInfo userInfo) {
		Boolean isAuth = false;
		if (Objects.equals(config.getWhiteListSourceType(), "entity")) {
			isAuth = userInfo.getAllowLogin() == StatusEnum.ACTIVE;
		} else {
			String value = connection.sync().hget("", userInfo.getUiId().toString());
			if (config.getWhiteListType()== WhiteListConfig.WhiteListType.WHITE) {
				isAuth = Objects.nonNull(value);
			} else {
				isAuth = Objects.isNull(value);
			}
		}
		if (!isAuth) {
			return config.getWhiteListType()== WhiteListConfig.WhiteListType.WHITE ? whiteResultObject : blackResultObject;
		}
		return null;
	}
}
