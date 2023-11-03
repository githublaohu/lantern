package com.lamp.lantern.plugins.core.environment;

import com.lamp.lantern.plugins.core.servlet.LanternServlet;

/**
 * 环境上下文, LanternServlet是当前处理的servlet
 */
public interface EnvironmentContext {

	Object getBean(String name) throws Exception;
	
	Object getBean(Class<?> clazz) throws Exception;

	public LanternServlet getLanternServlet();
}
