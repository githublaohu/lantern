package com.lamp.lantern.plugins.springmvc.autoconfigure;

import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.environment.SpringEnvironmentContext;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.springmvc.support.AuthenticationInterceptor;
import com.lamp.lantern.plugins.springmvc.support.LanternAuthenticationUserMethodArgumentResolver;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1.
 *
 * @author hahaha
 */
@Configuration
@EnableConfigurationProperties(LanternProperties.class)
public class LanternAuthenticationSupportConfigure implements ApplicationContextAware {

    private LanternProperties lanternProperties;

    private ApplicationContext applicationContext;


    @Bean
    public AuthenticationManager createAuthenticationManager() throws Exception {
        AuthenticationConfig authenticationConfig = lanternProperties.getAuthenticationConfig();
        SpringEnvironmentContext environmentContext = new SpringEnvironmentContext(applicationContext);
        return new AuthenticationManager(authenticationConfig,environmentContext);
    }

    /**
     * 登录校验拦截器
     */
    @Bean
    public AuthenticationInterceptor loginRequiredInterceptor(AuthenticationManager authenticationManager) {
        return new AuthenticationInterceptor(authenticationManager);
    }

    @Bean
    public LanternAuthenticationUserMethodArgumentResolver lanternAuthenticationUserMethodArgumentResolver() {
        return new LanternAuthenticationUserMethodArgumentResolver();
    }

    @Bean
    public HandlerService createHandlerService() throws Exception {
        HandlerService handlerService = new HandlerService();
        handlerService.setEnvironmentContext(new SpringEnvironmentContext(applicationContext));
        handlerService.createConnection(lanternProperties.getHandlerRedisMap());
        handlerService.createHandlerExecute(lanternProperties.getLoginConfig(),
                new SpringEnvironmentContext(applicationContext));
        return handlerService;
    }


    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
