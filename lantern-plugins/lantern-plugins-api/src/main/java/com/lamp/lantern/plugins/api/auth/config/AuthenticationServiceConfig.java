package com.lamp.lantern.plugins.api.auth.config;

import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import lombok.Data;

@Data
public class AuthenticationServiceConfig {

    private DubboAuthenticationConfig dubboAuthenticationConfig;

    private RedisCacheConfig redisCacheConfig;

    private AuthenticationService authenticationService;

    private LocalCacheConfig localCacheConfig;

    private String authenticationServiceName;

    private String authenticationServiceClassName;

}
