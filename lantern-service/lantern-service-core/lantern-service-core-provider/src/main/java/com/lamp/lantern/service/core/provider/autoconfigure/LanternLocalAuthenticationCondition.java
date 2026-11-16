package com.lamp.lantern.service.core.provider.autoconfigure;

import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Enables local database defaults when authentication is enabled and no alternative service is selected.
 */
public class LanternLocalAuthenticationCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return isLocalDatabaseMode(context.getEnvironment());
    }

    public static boolean isLocalDatabaseMode(Environment environment) {
        Binder binder = Binder.get(environment);
        if (!binder.bind("lantern.authentication-config.enable", Boolean.class).orElse(false)) {
            return false;
        }
        AuthenticationConfig config = binder.bind("lantern.authentication-config", AuthenticationConfig.class)
                .orElseGet(AuthenticationConfig::new);
        AuthenticationServiceConfig defaultService = config.getDefaultAuthenticationService();
        if (defaultService != null) {
            return "lanternDubboAuthentication".equals(defaultService.getAuthenticationServiceName());
        }
        return config.getAuthenticationServiceConfig() == null && config.getUserInfoAuthenticationServiceConfig() == null
                && !StringUtils.hasText(config.getAuthenticationFlowConfig().getPolicyServiceName());
    }
}
