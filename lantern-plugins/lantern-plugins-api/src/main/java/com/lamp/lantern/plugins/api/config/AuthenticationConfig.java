package com.lamp.lantern.plugins.api.config;

import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationType;

import com.lamp.lantern.plugins.api.auth.config.AuthenticationDataConfig;
import java.util.Objects;
import java.util.Set;

import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import lombok.Data;

/**
 * @author laohu
 */
@Data
public class AuthenticationConfig {

    /** Allow the host's Redis auto-configuration in local database mode. */
    private boolean redisAutoConfigurationEnabled;

    private AuthenticationFlowConfig authenticationFlowConfig = new AuthenticationFlowConfig();

    private AuthenticationType authenticationType;

    private AuthenticationDataConfig authenticationDataConfig;

    private AuthenticationServiceConfig defaultAuthenticationService;

    private AuthenticationServiceConfig userInfoAuthenticationServiceConfig;

    private AuthenticationServiceConfig authenticationServiceConfig;

}
