package com.lamp.lantern.service.core.provider.bootstrap;

import com.lamp.lantern.service.core.provider.autoconfigure.LanternDatabaseAuthenticationAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/** Register after the host's data source and MyBatis, without scanning its business components. */
@Configuration
@AutoConfigureAfter(name = {
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration"
})
@Import(LanternDatabaseAuthenticationAutoConfiguration.class)
public class LanternDatabaseAutoConfiguration {
}
