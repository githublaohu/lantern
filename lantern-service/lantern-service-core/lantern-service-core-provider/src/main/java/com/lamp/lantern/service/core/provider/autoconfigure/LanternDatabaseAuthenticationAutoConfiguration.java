package com.lamp.lantern.service.core.provider.autoconfigure;

import com.lamp.lantern.service.core.provider.configuration.LanternLocalDubboConfiguration;
import com.lamp.lantern.service.core.provider.configuration.LanternDatabaseLoginConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Component-scanned entry point: loads defaults before importing database and Dubbo configuration.
 */
@Configuration
@Conditional(LanternLocalAuthenticationCondition.class)
@PropertySource("classpath:lantern-local-authentication.properties")
@Import({LanternLocalDubboConfiguration.class, LanternDatabaseLoginConfiguration.class})
public class LanternDatabaseAuthenticationAutoConfiguration {
}
