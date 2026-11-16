package com.lamp.lantern.service.core.provider.bootstrap;

import com.lamp.lantern.service.core.provider.autoconfigure.LanternLocalAuthenticationCondition;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * The JDBC mode needs no Redis infrastructure. Explicit host Redis settings and beans remain supported;
 * existing spring.autoconfigure.exclude values are never replaced.
 */
public class LanternRedisAutoConfigurationFilter implements AutoConfigurationImportFilter, EnvironmentAware {

    private static final Set<String> REDIS_CONFIGURATIONS = new HashSet<>(Arrays.asList(
            "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"));

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean[] match(String[] configurations, AutoConfigurationMetadata metadata) {
        Binder binder = Binder.get(environment);
        boolean allowRedis = binder.bind("lantern.authentication-config.redis-auto-configuration-enabled", Boolean.class)
                .orElse(false)
                || binder.bind("spring.redis", Map.class).isBound()
                || binder.bind("spring.data.redis", Map.class).isBound();
        boolean databaseOnly = !allowRedis && LanternLocalAuthenticationCondition.isLocalDatabaseMode(environment);
        boolean[] matches = new boolean[configurations.length];
        for (int i = 0; i < configurations.length; i++) {
            matches[i] = !databaseOnly || !REDIS_CONFIGURATIONS.contains(configurations[i]);
        }
        return matches;
    }
}
