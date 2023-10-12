package com.lamp.lantern.plugins.core.login.config;

import java.util.Map;

import lombok.Data;

/**
 * 
 * @author laohu
 *
 */

/**
 * HandlerConfig是创建Handler的配置
 */
@Data
public class HandlerConfig {

	/**
	 * 系统名
	 * 至少要指定一个系统名, 用于创建Handler
	 */
	private String handlerName;
	
	private String className;
	
	private String beanName;
	
	private String beanClass;
	
	private Object handlerObject;
	
	private String redisName;
	/**
	 * 这里的ConfigMap对应于每个Handler的Config
	 */
	private Map<String,String> configMap;
	
}
