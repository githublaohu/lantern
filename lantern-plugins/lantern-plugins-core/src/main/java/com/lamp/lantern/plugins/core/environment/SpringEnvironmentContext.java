package com.lamp.lantern.plugins.core.environment;

import com.lamp.lantern.plugins.core.servlet.LanternServlet;
import com.lamp.lantern.plugins.core.servlet.SpringMVCServlet;
import org.springframework.context.ApplicationContext;

public class SpringEnvironmentContext implements EnvironmentContext {

	private ApplicationContext applicationContext;
	
	public SpringEnvironmentContext(ApplicationContext applicationContext) {
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

	@Override
	public LanternServlet getLanternServlet() {
		return new SpringMVCServlet();
	}

}
