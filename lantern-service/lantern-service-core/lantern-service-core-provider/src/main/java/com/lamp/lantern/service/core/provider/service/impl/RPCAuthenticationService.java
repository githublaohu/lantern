package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author laohu
 */
@Service
public class RPCAuthenticationService implements AuthenticationService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        return authenticationManager.getAuthenticationService().getUserInfo(authData);
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        return authenticationManager.getAuthenticationService().authentication(authData);
    }

    public AuthenticationServiceResult authenticationFlow(AuthenticationData authData){
        //authenticationManager.authentication()
        return null;
    }
}
