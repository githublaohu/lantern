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
package com.lamp.lantern.plugins.core.login.namelist;

import java.util.Objects;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.enums.StatusEnum;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;

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
			isAuth = userInfo.getUiAllowLogin() == StatusEnum.ACTIVE;
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
