package com.lamp.lantern.plugins.springmvc.bootstrap;

import com.lamp.lantern.plugins.springmvc.autoconfigure.LanternAuthenticationSupportConfigure;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/** Standard starter entry point; also supports hosts already scanning the configuration package. */
@Configuration
@AutoConfigureAfter(name = "com.lamp.lantern.service.core.provider.bootstrap.LanternDatabaseAutoConfiguration")
@Import(LanternAuthenticationSupportConfigure.class)
public class LanternAuthenticationAutoConfiguration {
}
