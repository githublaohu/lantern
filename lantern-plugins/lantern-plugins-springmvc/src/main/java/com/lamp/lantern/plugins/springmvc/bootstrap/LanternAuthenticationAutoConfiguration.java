package com.lamp.lantern.plugins.springmvc.bootstrap;

import com.lamp.lantern.plugins.springmvc.autoconfigure.LanternAuthenticationSupportConfigure;
import com.lamp.lantern.plugins.springmvc.autoconfigure.LanternProperties;
import com.lamp.lantern.plugins.springmvc.support.LanternDatabaseLoginController;
import com.lamp.lantern.plugins.springmvc.support.LanternEnterpriseWechatController;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatConfig;
import com.lamp.lantern.plugins.core.login.EnterpriseWechatLogin;
import com.lamp.lantern.plugins.core.login.HandlerExecute;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/** Standard starter entry point; also supports hosts already scanning the configuration package. */
@Configuration
@AutoConfigureAfter(name = "com.lamp.lantern.service.core.provider.bootstrap.LanternDatabaseAutoConfiguration")
@Import(LanternAuthenticationSupportConfigure.class)
public class LanternAuthenticationAutoConfiguration {

    /** Evaluate login defaults after provider auto-configuration, also for hosts scanning the support package. */
    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(name = "lantern.loginConfig.enable", havingValue = "true")
    static class DatabaseLoginConfiguration {

        @Bean("lanternDatabaseLoginController")
        @ConditionalOnProperty(name = "lantern.loginConfig.sessionStore", havingValue = "database")
        public LanternDatabaseLoginController databaseLoginController(
                @Qualifier("lanternDatabaseLogin") HandlerExecute login,
                @Qualifier("lanternDatabaseLogout") Consumer<String> logout, LanternProperties properties) {
            return new LanternDatabaseLoginController(login, logout,
                    properties.getAuthenticationConfig().getAuthenticationFlowConfig());
        }

        @Bean("lanternEnterpriseWechatController")
        @ConditionalOnProperty(name = "lantern.login-config.enterprise-wechat.enabled", havingValue = "true")
        public LanternEnterpriseWechatController enterpriseWechatController(EnterpriseWechatLogin login,
                EnterpriseWechatConfig config) {
            return new LanternEnterpriseWechatController(login, config);
        }
    }
}
