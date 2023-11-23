package com.lamp.lantern.plugins.api.auth.config;

import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author laohu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticationDataConfig extends  AbstractAuthenticationConfig{

    private Long dataSyncInterval;

    private AuthenticationDataService authenticationDataService;
}
