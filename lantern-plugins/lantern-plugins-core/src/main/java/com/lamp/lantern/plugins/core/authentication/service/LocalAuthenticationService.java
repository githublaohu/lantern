package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.LanternAuthCachePool;

/**
 * @author laohu
 */
public class LocalAuthenticationService implements AuthenticationService {

    private LanternAuthCachePool lanternAuthCachePool;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        return null;
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        return null;
    }
}
