package com.lamp.lantern.plugins.core.login;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.AuthChannelConfig;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.api.service.LanternUserInfoService;
import com.lamp.lantern.plugins.auth.platform.AlipayPlatformAuthService;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatPlatformAuthService;
import com.lamp.lantern.plugins.auth.qrcode.QrcodeAuthService;
import com.lamp.lantern.plugins.auth.thrid.*;
import com.lamp.lantern.plugins.core.environment.EnvironmentContext;
import com.lamp.lantern.plugins.core.exception.CreateHandlerException;
import com.lamp.lantern.plugins.core.login.broadcast.BroadcastAuthHandler;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LanternUserInfoConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.login.exclusive.ExclusiveAuthHandler;
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

    private final static Map<String, Class<?>> LOGIN_CHANNEL_CACHE = new HashMap<>();


    static {
        setClassCache(BroadcastAuthHandler.class);
        setClassCache(WhiteListAuthHandler.class);
        setClassCache(LoginRecordAuthHandler.class);
        setClassCache(LoginTimesAuthHandler.class);
        setClassCache(CreateTokenAuthHandler.class);
        setClassCache(ExclusiveAuthHandler.class);
    }
    static {
        setLoginCache(AlipayPlatformAuthService.class);
        setLoginCache(AliPayThirdAuthService.class);
        setLoginCache(BaiduThirdAuthService.class);
        setLoginCache(EnterpriseWechatPlatformAuthService.class);
        setLoginCache(QQThirdAuthService.class);
        setLoginCache(TaobaoThirdAuthService.class);
        setLoginCache(WechatThirdAuthService.class);
        setLoginCache(GithubThirdAuthService.class);
        setLoginCache(QrcodeAuthService.class);
    }


    private Map<String, StatefulRedisConnection<String, String>> connectionClientCache = new HashMap<>();

    /**
     * LoginConfig -> HandlerExecute
     */
    private Map<String, HandlerExecute> handlerExecuteMap = new ConcurrentHashMap<>();

    @Setter
    private EnvironmentContext environmentContext;

    private boolean objectByEnvironment = false;

    public static void setClassCache(Class<?> clazz) {
        CLASS_CACHE.put(clazz.getSimpleName(), clazz);
    }
    public static void setLoginCache(Class<?> clazz) {
        LOGIN_CHANNEL_CACHE.put(clazz.getSimpleName(), clazz);
    }

    /**
     * 通过配置设置Handler使用的redis连接
     *
     * @param configMap
     */
    public void createConnection(Map<String, String> configMap) {
        for (Entry<String, String> entry : configMap.entrySet()) {
            log.info("create connection , name is {} url is {}", entry.getKey(), entry.getValue());
            connectionClientCache.put(entry.getKey(), this.createConnection(entry.getValue()));
        }
    }

    private StatefulRedisConnection<String, String> createConnection(String config) {
        return RedisClient.create(config).connect();
    }

    /**
     * Get HandlerExecute by loginConfig::systemName
     *
     * @param loginConfig 登录配置
     * @return 从缓存中获取HandlerExecute
     */
    public HandlerExecute getHandlerExecute(String loginConfig) {
        return handlerExecuteMap.get(loginConfig);
    }

    public HandlerExecute createHandlerExecute(LoginConfig loginConfig, EnvironmentContext environmentContext) throws Exception {
        log.info("create handlerExecute , loginConfig is {}", loginConfig);
        HandlerExecute handlerExecute = new HandlerExecute();
        handlerExecute.setLoginConfig(loginConfig);
        handlerExecute.setHandlerList(this.createHandler(loginConfig, environmentContext));
        handlerExecute.setServlet(environmentContext.getLanternServlet());
        handlerExecute.setAuthServiceMap(this.createAuthServiceMap(loginConfig.getAuthChannelConfigList()));
        handlerExecute.setLanternUserInfoService(this.createLanternUserInfoService(loginConfig.getLanternUserInfoConfig()));
        handlerExecute.setSessionWorkInfo(LanternContext.getContext().getSessionWorkInfo());
        handlerExecuteMap.put(loginConfig.getSystemName(), handlerExecute);
        return handlerExecute;
    }

    private Map<LoginType,Map<String,AuthService>> createAuthServiceMap(List<AuthChannelConfig> authChannelConfigList) throws Exception {

        Map<LoginType,Map<String,AuthService>>  authServiceMap = new HashMap<>();
        Arrays.stream(LoginType.values()).forEach(t ->{ authServiceMap.put(t,new HashMap<>());});
        for (AuthChannelConfig authChannelConfig : authChannelConfigList) {
            AuthService authService = this.createAuthService(authChannelConfig);
            AuthTypeChannel authTypeChannel = authService.getClass().getAnnotation(AuthTypeChannel.class);
            authServiceMap.get(authTypeChannel.loginType()).put(authTypeChannel.authChannel(),authService);
        }
        return authServiceMap;
    }

    private AuthService createAuthService(AuthChannelConfig authChannelConfig)
            throws Exception {
        AuthService authService = null;
        if (Objects.nonNull(authChannelConfig.getSimpleClassName())) {
            authService = this.createAuthService(LOGIN_CHANNEL_CACHE.get(authChannelConfig.getSimpleClassName()), authChannelConfig);
        } else if (Objects.nonNull(authChannelConfig.getClassName())) {
            Class<?> clazz = Class.forName(authChannelConfig.getClassName());
            authService = this.createAuthService(clazz, authChannelConfig);
        } else if (Objects.nonNull(authChannelConfig.getBeanName())) {
            authService = (AuthService) environmentContext.getBean(authChannelConfig.getBeanName());
        } else if (Objects.nonNull(authChannelConfig.getBeanClass())) {
            authService = (AuthService) environmentContext.getBean(authChannelConfig.getBeanClass());
        }
        if (Objects.isNull(authService)) {
            // TODO error
            throw new CreateHandlerException("");
        }
        authService.initialization(authChannelConfig);
        return authService;
    }

    private AuthService createAuthService(Class<?> clazz, AuthChannelConfig authChannelConfig)
            throws Exception {
        AuthService authService = (AuthService) (this.objectByEnvironment ? environmentContext.getBean(clazz) : clazz.newInstance());
        return authService;
    }

    @SuppressWarnings("unchecked")
    private List<AuthHandler> createHandler(LoginConfig loginConfig, EnvironmentContext environmentContext)
            throws Exception {
        List<AuthHandler> authHandlers = new ArrayList<>();

        for (HandlerConfig handlerConfig : loginConfig.getHandlerConfigList()) {
            AbstractAuthHandler<Object> authHandler = null;
            if (Objects.nonNull(handlerConfig.getHandlerName())) {
                authHandler = this.createAuthHandler(CLASS_CACHE.get(handlerConfig.getHandlerName()), handlerConfig, environmentContext);
            } else if (Objects.nonNull(handlerConfig.getClassName())) {
                Class<?> clazz = Class.forName(handlerConfig.getClassName());
                authHandler = this.createAuthHandler(clazz, handlerConfig, environmentContext);
            } else if (Objects.nonNull(handlerConfig.getBeanName())) {
                authHandler = (AbstractAuthHandler<Object>) environmentContext.getBean(handlerConfig.getBeanName());
            } else if (Objects.nonNull(handlerConfig.getBeanClass())) {
                authHandler = (AbstractAuthHandler<Object>) environmentContext.getBean(handlerConfig.getBeanClass());
            }
            authHandler.setHandlerName(handlerConfig.getHandlerName());
            authHandler.setSystemName(loginConfig.getSystemName());
            authHandler.init();

            if (Objects.isNull(authHandler)) {
                // TODO error
                throw new CreateHandlerException("");
            }
            authHandlers.add(authHandler);
        }
        return authHandlers;
    }

    private LanternUserInfoService createLanternUserInfoService (LanternUserInfoConfig lanternUserInfoConfig) throws Exception {
        LanternUserInfoService lanternUserInfoService = null;
        if (Objects.nonNull(lanternUserInfoConfig.getLanternUserInfoService())){
            lanternUserInfoService = lanternUserInfoConfig.getLanternUserInfoService();
        }
        else if (Objects.nonNull(lanternUserInfoConfig.getBeanClass())){
            lanternUserInfoService = (LanternUserInfoService) environmentContext.getBean(lanternUserInfoConfig.getBeanClass());
        }
        else if (Objects.nonNull(lanternUserInfoConfig.getBeanName())){
            lanternUserInfoService = (LanternUserInfoService) environmentContext.getBean(lanternUserInfoConfig.getBeanName());
        }
        return lanternUserInfoService;
    }

    /**
     * @param clazz              AuthHandler的class
     * @param handlerConfig      对应Handler的Config
     * @param environmentContext
     * @return AuthHandler
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private AbstractAuthHandler<Object> createAuthHandler(Class<?> clazz, HandlerConfig handlerConfig, EnvironmentContext environmentContext)
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
        AbstractAuthHandler<Object> abstractAuthHandler = null;
        if (this.objectByEnvironment) {
            abstractAuthHandler = (AbstractAuthHandler<Object>) this.environmentContext.getBean(clazz);
        } else {
            abstractAuthHandler = (AbstractAuthHandler<Object>) clazz.newInstance();
        }
        if (Objects.equals(configClazz, Object.class)) {
            if (Objects.isNull(handlerConfig.getConfigMap())) {
                // TODO errer
                throw new CreateHandlerException("");
            }
        } else {
            JSONObject object = new JSONObject();
            object.putAll(handlerConfig.getConfigMap());
            abstractAuthHandler.setConfig(object.toJavaObject(configClazz));

        }
        StatefulRedisConnection<String, String> connection = connectionClientCache.get(handlerConfig.getRedisName());
        if (Objects.isNull(connection)) {
            // TODO errer
            throw new CreateHandlerException("");
        }
        abstractAuthHandler.setConnection(connection);
        return abstractAuthHandler;
    }
}
