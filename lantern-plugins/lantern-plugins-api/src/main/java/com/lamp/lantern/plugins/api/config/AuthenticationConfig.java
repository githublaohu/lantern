package com.lamp.lantern.plugins.api.config;

import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationType;

import java.util.Objects;
import java.util.Set;

import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import lombok.Data;

/**
 * @author laohu
 */
@Data
public class AuthenticationConfig {

    private String systemId;

    private String systemName;

    private Set<String> notAuthentication;

    private Set<String> userAuthentication;

    private int authentication;

    private boolean resourcesAuthentication = false;

    private String redirectData;

    /**
     * token位置 "cookie" or "header"
     */
    private String tokenSpot;

    private String tokenName;

    private String redirectSpot;

    private AuthenticationType authenticationType;

    private AuthenticationDataService authenticationDataService;

    private Long dataSyncInterval;

    private AuthenticationService authenticationService;

    private String authenticationServiceName;

    private AuthenticationServiceConfig userInfoAuthenticationServiceConfig;

    private AuthenticationServiceConfig authenticationServiceConfig;

    public boolean isUserAuthenticationServiceConfig() {
        return Objects.nonNull(this.userInfoAuthenticationServiceConfig) && Objects.nonNull(this.userInfoAuthenticationServiceConfig.getAuthenticationServiceName());
    }

    public boolean isAuthenticationServiceConfig() {
        return Objects.nonNull(this.authenticationServiceConfig) && Objects.nonNull(this.authenticationServiceConfig.getAuthenticationServiceName());
    }
}
