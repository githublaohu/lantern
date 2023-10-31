package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;

/**
 * @author laohu
 */
public class LocalAuthenticationService implements AuthenticationService {


    private AuthenticationDataService authOperation;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        return null;
    }

    @Override
    public AuthenticationServiceResult authentication(com.lamp.lantern.plugins.api.auth.AuthenticationData authData) {

        return null;
    }
}
