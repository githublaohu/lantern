package com.lamp.lantern.plugins.api.auth.config;

import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import lombok.Data;

/**
 * @author hahaha
 */
@Data
public class AuthenticationServiceConfig extends  AbstractAuthenticationConfig {

    private AuthenticationService authenticationService;

    private LocalCacheConfig localCacheConfig;


}
