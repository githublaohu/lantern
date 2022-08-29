package com.lamp.lantern.service.action.login.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.service.action.login.config.HandlerConfig;
import com.lamp.lantern.service.action.login.config.LoginConfig;
import com.lamp.lantern.service.action.login.handler.config.RedisConfig;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import io.lettuce.core.RedisClient;

@Component
public class HandlerService implements ApplicationContextAware{

	private final static Map<String,HandlerInfo> classCache = new HashMap<>();
	
	@Autowired
	private RedisConfig redisConfig;
	
	private ApplicationContext applicationContext;
	
	private Map<String ,RedisClient> redisClientCche = new HashMap<>();
	
	static{
		// 通过url 读取所有的class文件，然后通过
		try {
			Class.forName("");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() throws InstantiationException, IllegalAccessException {
		
		LoginConfig loginConfig = new LoginConfig();
		
		for(HandlerConfig handlerConfig :loginConfig.getHandlerConfig() ) {
			HandlerInfo handlerInfo = classCache.get(handlerConfig.getName());
			AbstrackAuthHandler<Object> abstrackAuthHandler= (AbstrackAuthHandler<Object>)applicationContext.getBean(handlerInfo.clazz);
			
			JSONObject object = new JSONObject(handlerConfig.getConfig());
			
			object.toJavaObject(handlerInfo.config);
			handlerConfig.getRedisName();
			
			abstrackAuthHandler.setConfig(object.toJavaObject(handlerInfo.config));
	
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	
	public HandlerExecute getHandlerExecute(UserInfoEntity userInfoEntity) {
		HandlerExecute handlerExecute = new HandlerExecute();
		
		return handlerExecute;
	}
	
	static class HandlerInfo{
		Class<?> clazz;
				
		Class<?> config;
		
	}
	
}
