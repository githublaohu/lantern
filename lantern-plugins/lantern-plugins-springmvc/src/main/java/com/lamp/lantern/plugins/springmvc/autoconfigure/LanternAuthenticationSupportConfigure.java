package com.lamp.lantern.plugins.springmvc.autoconfigure;

import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.environment.SpringEnvironmentContext;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.springmvc.support.AuthenticationInterceptor;
import com.lamp.lantern.plugins.springmvc.support.LanternAuthenticationUserMethodArgumentResolver;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * 1.
 */
@Configuration
@EnableConfigurationProperties(LanternProperties.class)
public class LanternAuthenticationSupportConfigure  implements ApplicationContextAware {

    private LanternProperties lanternProperties;

    private ApplicationContext applicationContext;

    /**
     * 登录校验拦截器
     *
     */
    @Bean
    public AuthenticationInterceptor loginRequiredInterceptor() {
        AuthenticationDataService authOperationClass = null;
        AuthenticationConfig authenticationConfig = lanternProperties.getAuthenticationConfig();
//        if (Objects.nonNull(authenticationConfig)) {
//            authOperationClass = (AuthenticationDataService) applicationContext.getBean(lanternProperties.getAuthOperation());
//        }
        if(Objects.nonNull(lanternProperties.getAuthenticationConfig().getAuthenticationServiceName())) {
            AuthenticationService authenticationService = (AuthenticationService) applicationContext.getBean(authenticationConfig.getAuthenticationServiceName());
            authenticationConfig.setAuthenticationService(authenticationService);
        }else if(authenticationConfig.isAuthenticationServiceConfig()){
            AuthenticationService authenticationService = (AuthenticationService) applicationContext.getBean(authenticationConfig.getAuthenticationServiceConfig().getAuthenticationServiceName());
            authenticationConfig.getAuthenticationServiceConfig().setAuthenticationService(authenticationService);

        }else if(authenticationConfig.isUserAuthenticationServiceConfig()) {
            AuthenticationService authenticationService = (AuthenticationService) applicationContext.getBean(authenticationConfig.getUserInfoAuthenticationServiceConfig().getAuthenticationServiceName());
            authenticationConfig.getUserInfoAuthenticationServiceConfig().setAuthenticationService(authenticationService);

        }
        AuthenticationManager authenticationManager = new AuthenticationManager(lanternProperties.getAuthenticationConfig());
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
        handlerService.createHandlerExecute(lanternProperties.getLoginConfig(), new SpringEnvironmentContext(applicationContext));
        return handlerService;
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
