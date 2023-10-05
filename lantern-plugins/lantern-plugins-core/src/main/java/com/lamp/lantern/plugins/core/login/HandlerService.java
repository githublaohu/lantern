package com.lamp.lantern.plugins.core.login;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.api.config.AuthChannelCofing;
import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.core.environment.EnvironmentContext;
import com.lamp.lantern.plugins.core.exception.CreateHandlerException;
import com.lamp.lantern.plugins.core.login.broadcast.BroadcastAuthHandler;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.login.namelist.WhiteListAuthHandler;
import com.lamp.lantern.plugins.core.login.record.LoginRecordAuthHandler;
import com.lamp.lantern.plugins.core.login.times.LoginTimesAuthHandler;
import com.lamp.lantern.plugins.core.login.token.CreateTokenAuthHandler;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录处理的核心类,负责创建和管理HandlerExecute.
 */
@Slf4j
public class HandlerService {

    private final static Map<String, Class<?>> CLASS_CACHE = new HashMap<>();

    static {
        // 通过url 读取所有的class文件，然后通过
        setClassCache(BroadcastAuthHandler.class);
        setClassCache(WhiteListAuthHandler.class);
        setClassCache(LoginRecordAuthHandler.class);
        setClassCache(LoginTimesAuthHandler.class);
        setClassCache(CreateTokenAuthHandler.class);
    }

    private Map<String, StatefulRedisConnection<String, String>> connectionClientCache = new HashMap<>();

    private Map<String, HandlerExecute> handlerExecuteMap = new ConcurrentHashMap<>();

    @Setter
    private EnvironmentContext environmentContext;

    private boolean objectByEnvironment = false;

    public static void setClassCache(Class<?> clazz) {
        CLASS_CACHE.put(clazz.getSimpleName(), clazz);
    }

    public void createConnection(Map<String, String> configMap) {
        for (Entry<String, String> entry : configMap.entrySet()) {
            log.info("create connection , name is {} url is {}", entry.getKey(), entry.getValue());
            connectionClientCache.put(entry.getKey(), this.createConnection(entry.getValue()));
        }
    }

    private StatefulRedisConnection<String, String> createConnection(String config) {
        return RedisClient.create(config).connect();
    }

    public HandlerExecute getHandlerExecute(String loginConfig) {
        return handlerExecuteMap.get(loginConfig);
    }

    public HandlerExecute createHandlerExecute(LoginConfig loginConfig, EnvironmentContext environmentContext) throws Exception {
        log.info("login config is {}", loginConfig);
        HandlerExecute handlerExecute = new HandlerExecute();
        handlerExecute.setLoginConfig(loginConfig);
        handlerExecute.setHandlerList(this.createHandler(loginConfig, environmentContext));
        handlerExecute.setServlet(environmentContext.getLanternServlet());
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
            authService = (AuthService) environmentContext.getBean(authChannelCofing.getBeanName());
        } else if (Objects.nonNull(authChannelCofing.getBeanClass())) {
            authService = (AuthService) environmentContext.getBean(authChannelCofing.getBeanClass());
        }
        if (Objects.isNull(authService)) {
            // TODO errer
            throw new CreateHandlerException("");
        }
        return authService;
    }

    private AuthService createAuthService(Class<?> clazz, AuthChannelCofing authChannelCofing)
            throws Exception {
        AuthService authService = (AuthService) (this.objectByEnvironment ? environmentContext.getBean(clazz) : clazz.newInstance());
        authService.initialization(authChannelCofing);
        return authService;
    }

    @SuppressWarnings("unchecked")
    private List<AuthHandler> createHandler(LoginConfig loginConfig, EnvironmentContext environmentContext)
            throws Exception {
        List<AuthHandler> authHandlers = new ArrayList<>();

        for (HandlerConfig handlerConfig : loginConfig.getHandlerConfigList()) {
            AbstrackAuthHandler<Object> authHandler = null;
            if (Objects.nonNull(handlerConfig.getHandlerName())) {
                authHandler = this.createAuthHandler(CLASS_CACHE.get(handlerConfig.getHandlerName()), handlerConfig, environmentContext);
            } else if (Objects.nonNull(handlerConfig.getClassName())) {
                Class<?> clazz = Class.forName(handlerConfig.getClassName());
                authHandler = this.createAuthHandler(clazz, handlerConfig, environmentContext);
            } else if (Objects.nonNull(handlerConfig.getBeanName())) {
                authHandler = (AbstrackAuthHandler<Object>) environmentContext.getBean(handlerConfig.getBeanName());
            } else if (Objects.nonNull(handlerConfig.getBeanClass())) {
                authHandler = (AbstrackAuthHandler<Object>) environmentContext.getBean(handlerConfig.getBeanClass());
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
    private AbstrackAuthHandler<Object> createAuthHandler(Class<?> clazz, HandlerConfig handlerConfig, EnvironmentContext environmentContext)
            throws Exception {
        Type type;
        if (clazz.getGenericInterfaces().length == 0) {
            type = clazz.getGenericSuperclass();
        } else {
            type = clazz.getGenericInterfaces()[0];
        }
        if (Objects.isNull(type)) {

        }
        Class<?> configClazz = (Class<?>) ((ParameterizedType) (type))
                .getActualTypeArguments()[0];
        AbstrackAuthHandler<Object> abstrackAuthHandler = null;
        if (this.objectByEnvironment) {
            abstrackAuthHandler = (AbstrackAuthHandler<Object>) this.environmentContext.getBean(clazz);
        } else {
            abstrackAuthHandler = (AbstrackAuthHandler<Object>) clazz.newInstance();
        }
        if (Objects.equals(configClazz, Object.class)) {
            if (Objects.isNull(handlerConfig.getConfigMap())) {
                // TODO errer
                throw new CreateHandlerException("");
            }
        } else {
            JSONObject object = new JSONObject();
            object.putAll(handlerConfig.getConfigMap());
            abstrackAuthHandler.setConfig(object.toJavaObject(configClazz));

        }
        StatefulRedisConnection<String, String> connection = connectionClientCache.get(handlerConfig.getRedisName());
        if (Objects.isNull(connection)) {
            // TODO errer
            throw new CreateHandlerException("");
        }
        abstrackAuthHandler.setConnection(connection);
        return abstrackAuthHandler;
    }
}
