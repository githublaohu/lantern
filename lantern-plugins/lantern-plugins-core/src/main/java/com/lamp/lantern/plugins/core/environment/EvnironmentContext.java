package com.lamp.lantern.plugins.core.environment;

public interface EvnironmentContext {

	Object getBean(String name) throws Exception;
	
	Object getBean(Class<?> clazz) throws Exception;
}
