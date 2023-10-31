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

import java.util.Map;

import lombok.Data;

/**
 * 
 * @author laohu
 *
 */
@Data
public class HandlerConfig {

	/**
	 * 系统名
	 */
	private String handlerName;
	
	private String className;
	
	private String beanName;
	
	private String beanClass;
	
	private Object handlerObject;
	
	private String redisName;
	
	private Map<String,String> configMap;
	
}
