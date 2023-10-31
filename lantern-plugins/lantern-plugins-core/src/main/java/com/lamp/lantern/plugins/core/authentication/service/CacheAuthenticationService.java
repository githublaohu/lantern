package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * @author laohu
 */
public class CacheAuthenticationService implements AuthenticationService {

    protected StatefulRedisConnection<String, String> connection;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        return null;
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        return null;
    }
}
