package com.lamp.lantern.plugins.springmvc.autoconfigure;

import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.environment.SpringEnvironmentContext;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.springmvc.support.AuthenticationInterceptor;
import com.lamp.lantern.plugins.springmvc.support.LanternAuthenticationUserMethodArgumentResolver;
import com.lamp.lantern.plugins.springmvc.support.LanternSpringMVCBehavior;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 1.
 *
 * @author hahaha
 */
@Configuration
@EnableConfigurationProperties(LanternProperties.class)
public class LanternAuthenticationSupportConfigure implements ApplicationContextAware {

    @Autowired
    private LanternProperties lanternProperties;

    private ApplicationContext applicationContext;


    @Bean
    @ConditionalOnProperty(name = "lantern.authenticationConfig.enable",havingValue = "true")
    public AuthenticationManager createAuthenticationManager() throws Exception {
        AuthenticationConfig authenticationConfig = lanternProperties.getAuthenticationConfig();
        SpringEnvironmentContext environmentContext = new SpringEnvironmentContext(applicationContext);
        return new AuthenticationManager(authenticationConfig, environmentContext);
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(name = "lantern.authenticationConfig.enable", havingValue = "true")
    static class AuthenticationMvcConfiguration {

        @Bean
        public AuthenticationInterceptor loginRequiredInterceptor(AuthenticationManager authenticationManager) {
            return new AuthenticationInterceptor(authenticationManager);
        }

        @Bean
        public LanternAuthenticationUserMethodArgumentResolver lanternAuthenticationUserMethodArgumentResolver() {
            return new LanternAuthenticationUserMethodArgumentResolver();
        }

        @Bean
        public LanternSpringMVCBehavior lanternSpringMvcBehavior() {
            return new LanternSpringMVCBehavior();
        }

        @Bean
        public WebMvcConfigurer lanternAuthenticationConfigurer(LanternProperties properties, AuthenticationInterceptor interceptor,
                LanternAuthenticationUserMethodArgumentResolver resolver) {
            List<String> paths = properties.getAuthenticationConfig().getAuthenticationFlowConfig().getIncludePaths();
            Assert.notEmpty(paths, "Lantern includePaths must explicitly list protected paths");
            Assert.isTrue(paths.stream().allMatch(path -> path != null && path.startsWith("/")),
                    "Lantern includePaths must start with /");
            return new WebMvcConfigurer() {
                @Override
                public void addInterceptors(InterceptorRegistry registry) {
                    registry.addInterceptor(interceptor).addPathPatterns(paths.toArray(new String[0])).order(Ordered.HIGHEST_PRECEDENCE);
                }

                @Override
                public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                    resolvers.add(resolver);
                }
            };
        }
    }

    @Bean
    @ConditionalOnProperty(name = "lantern.loginConfig.enable",havingValue = "true")
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
