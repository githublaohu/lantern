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
