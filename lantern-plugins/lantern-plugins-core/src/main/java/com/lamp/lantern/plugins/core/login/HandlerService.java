package com.lamp.lantern.plugins.core.login;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.api.config.AuthChannelCofing;
import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.core.environment.EvnironmentContext;
import com.lamp.lantern.plugins.core.exception.CreateHandlerException;
import com.lamp.lantern.plugins.core.login.broadcast.BroadcastAuthHandler;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.login.namelist.WhiteListAuthHandler;
import com.lamp.lantern.plugins.core.login.record.LoginRecordAuthHandler;
import com.lamp.lantern.plugins.core.login.times.LoginTimesAuthHandler;
import com.lamp.lantern.plugins.core.login.token.CteateTockenAuthHandler;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerService  {

	private final static Map<String, Class<?>> CLASS_CACHE = new HashMap<>();

	static {
		// 通过url 读取所有的class文件，然后通过
		setClassCache(BroadcastAuthHandler.class);
		setClassCache(WhiteListAuthHandler.class);
		setClassCache(LoginRecordAuthHandler.class);
		setClassCache(LoginTimesAuthHandler.class);
		setClassCache(CteateTockenAuthHandler.class);
	}

	private Map<String, StatefulRedisConnection<String, String>> connectionClientCche = new HashMap<>();
	
	private Map<String ,HandlerExecute> handlerExecuteMap = new ConcurrentHashMap<>();
	
	@Setter
	private EvnironmentContext evnironmentContext;
	
	private boolean objectByEvnironment = false;
	
	public static void setClassCache(Class<?> clazz) {
		CLASS_CACHE.put(clazz.getSimpleName(), clazz);
	}

	public void createConnection(Map<String,String> configMap) {
		for(Entry<String, String> entry : configMap.entrySet()) {
			log.info("create connection , name is {} url is {}", entry.getKey() , entry.getValue());
			connectionClientCche.put(entry.getKey(), this.createCOnnection(entry.getValue()));
		}
	}
	
	private StatefulRedisConnection<String, String> createCOnnection(String config){
		return  RedisClient.create(config).connect();
	}

	public HandlerExecute getHandlerExecute(String loginConfig) {
		return handlerExecuteMap.get(loginConfig);
	}
	
	public HandlerExecute createHandlerExecute(LoginConfig loginConfig,EvnironmentContext evnironmentContext) throws Exception {
		log.info("login config is {}", loginConfig);
		HandlerExecute handlerExecute = new HandlerExecute();
		handlerExecute.setLoginConfig(loginConfig);
		handlerExecute.setHandlerList(this.createHandler(loginConfig,evnironmentContext));
		handlerExecute.setAuthService(this.createAuthService(loginConfig.getAuthChannelCofing()));
		handlerExecuteMap.put(loginConfig.getSystemName(), handlerExecute);
		return handlerExecute;
	}

	private AuthService createAuthService(AuthChannelCofing authChannelCofing)
			throws Exception {
		AuthService authService = null;
		if (Objects.nonNull(authChannelCofing.getAuthChannel())) {
			authService = this.createAuthService(null, authChannelCofing);
		} else if (Objects.nonNull(authChannelCofing.getClassName())) {
			Class<?> clazz = Class.forName(authChannelCofing.getClassName());
			authService = this.createAuthService(clazz, authChannelCofing);
		} else if (Objects.nonNull(authChannelCofing.getBeanName())) {
			authService = (AuthService) evnironmentContext.getBean(authChannelCofing.getBeanName());
		} else if (Objects.nonNull(authChannelCofing.getBeanClass())) {
			authService = (AuthService) evnironmentContext.getBean(authChannelCofing.getBeanClass());
		}
		if (Objects.isNull(authService)) {
			// TODO errer
			throw new CreateHandlerException("");
		}
		return authService;
	}

	private AuthService createAuthService(Class<?> clazz, AuthChannelCofing authChannelCofing)
			throws Exception {
		AuthService authService = (AuthService)(this.objectByEvnironment ? evnironmentContext.getBean(clazz): clazz.newInstance());
		authService.initialization(authChannelCofing);
		return authService;
	}

	@SuppressWarnings("unchecked")
	private List<AuthHandler> createHandler(LoginConfig loginConfig,EvnironmentContext evnironmentContext)
			throws Exception {
		List<AuthHandler> authHandlers = new ArrayList<>();

		for (HandlerConfig handlerConfig : loginConfig.getHandlerConfigList()) {
			AbstrackAuthHandler<Object> authHandler = null;
			if (Objects.nonNull(handlerConfig.getHandlerName())) {
				authHandler = this.createAuthHandler(CLASS_CACHE.get(handlerConfig.getHandlerName()), handlerConfig,evnironmentContext);
			} else if (Objects.nonNull(handlerConfig.getClassName())) {
				Class<?> clazz = Class.forName(handlerConfig.getClassName());
				authHandler = this.createAuthHandler(clazz, handlerConfig,evnironmentContext);
			} else if (Objects.nonNull(handlerConfig.getBeanName())) {
				authHandler = (AbstrackAuthHandler<Object>) evnironmentContext.getBean(handlerConfig.getBeanName());
			} else if (Objects.nonNull(handlerConfig.getBeanClass())) {
				authHandler = (AbstrackAuthHandler<Object>) evnironmentContext.getBean(handlerConfig.getBeanClass());
			}
			authHandler.setSystemName(loginConfig.getSystemName());
			authHandler.init();
			
			if (Objects.isNull(authHandler)) {
				// TODO errer
				throw new CreateHandlerException("");
			}
			authHandlers.add(authHandler);
		}
		return authHandlers;
	}

	@SuppressWarnings("unchecked")
	private AbstrackAuthHandler<Object>  createAuthHandler(Class<?> clazz, HandlerConfig handlerConfig,EvnironmentContext evnironmentContext)
			throws Exception {

		Class<?> configClazz = (Class<?>) ((ParameterizedType) (clazz.getGenericInterfaces()[0]))
				.getActualTypeArguments()[0];
		AbstrackAuthHandler<Object> abstrackAuthHandler = null;
		if(this.objectByEvnironment) {
			abstrackAuthHandler = (AbstrackAuthHandler<Object>)this.evnironmentContext.getBean(clazz);
		}else {
			abstrackAuthHandler = (AbstrackAuthHandler<Object>) clazz.newInstance();
		}
		if (Objects.equals(configClazz, Object.class)) {
			if (Objects.isNull(handlerConfig.getConfigMap())) {
				// TODO errer
				throw new CreateHandlerException("");
			}
			JSONObject object = new JSONObject();
			object.putAll(handlerConfig.getConfigMap());
			abstrackAuthHandler.setConfig(object.toJavaObject(configClazz));
		} else {

		}
		StatefulRedisConnection<String, String> connection = connectionClientCche.get(handlerConfig.getRedisName());
		if (Objects.isNull(connection)) {
			// TODO errer
			throw new CreateHandlerException("");
		}
		abstrackAuthHandler.setConnection(connection);
		return abstrackAuthHandler;
	}
}
