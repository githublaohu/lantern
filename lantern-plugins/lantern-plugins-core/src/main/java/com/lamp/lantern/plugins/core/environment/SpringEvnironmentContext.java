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
