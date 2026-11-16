package com.lamp.lantern.service.core.provider.bootstrap;

import com.lamp.lantern.service.core.provider.autoconfigure.LanternLocalAuthenticationCondition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/** Load overridable defaults before Boot decides whether to initialize the host database. */
@Configuration
@Conditional(LanternLocalAuthenticationCondition.class)
@PropertySource("classpath:lantern-local-authentication.properties")
@AutoConfigureBefore(name = {
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration",
        "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration"
})
public class LanternDatabaseDefaultsAutoConfiguration {
}
