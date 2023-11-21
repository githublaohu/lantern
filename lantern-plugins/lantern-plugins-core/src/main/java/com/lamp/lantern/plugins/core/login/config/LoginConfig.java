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
package com.lamp.lantern.plugins.core.login.config;

import java.util.List;

import com.lamp.lantern.plugins.api.config.AuthChannelConfig;

import lombok.Data;

@Data
public class LoginConfig {
	
	/**
	 * 系统名, 在初始化时会被加载到所有的Handler中, 而且作为Redis的前缀
	 */
	private String systemName;

	private List<HandlerConfig> handlerConfigList;
	
	private List<AuthChannelConfig> authChannelConfigList;

	private LanternUserInfoConfig lanternUserInfoConfig;
}
