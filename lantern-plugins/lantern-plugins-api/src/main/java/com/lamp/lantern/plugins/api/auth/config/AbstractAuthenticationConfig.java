package com.lamp.lantern.plugins.api.auth.config;

import com.lamp.lantern.plugins.api.auth.config.DubboAuthenticationConfig;
import com.lamp.lantern.plugins.api.auth.config.RedisCacheConfig;
import lombok.Data;

/**
 * @author laohu
 */
@Data
public abstract class AbstractAuthenticationConfig {

    private DubboAuthenticationConfig dubboAuthenticationConfig;

    private RedisCacheConfig redisCacheConfig;

    private String authenticationServiceName;

    private String authenticationServiceClassName;
}
