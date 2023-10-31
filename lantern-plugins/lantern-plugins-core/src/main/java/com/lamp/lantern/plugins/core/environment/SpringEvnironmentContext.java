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
package com.lamp.lantern.plugins.core.environment;

import org.springframework.context.ApplicationContext;

public class SpringEvnironmentContext implements EvnironmentContext{

	private ApplicationContext applicationContext;
	
	public SpringEvnironmentContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object getBean(String name) throws Exception {
		return applicationContext.getBean(name);
	}

	@Override
	public Object getBean(Class<?> clazz) throws Exception {
		return applicationContext.getBean(clazz);
	}

}
