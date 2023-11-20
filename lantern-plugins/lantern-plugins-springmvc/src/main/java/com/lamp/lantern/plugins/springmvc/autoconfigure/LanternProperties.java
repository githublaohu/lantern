package com.lamp.lantern.plugins.springmvc.autoconfigure;

import java.util.Map;

import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "lantern")
public class LanternProperties {
	/**
	 * 登录相关配置
	 */
	private LoginConfig loginConfig;

	/**
	 * Handler的Redis配置
	 * ("testTokenRedis", "redis://localhost:6379/0")
	 */
	private Map<String,String> handlerRedisMap;

	private AuthenticationConfig authenticationConfig;

}
