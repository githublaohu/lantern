package com.lamp.lantern.plugins.api.auth;

public interface AuthenticationRpcService {
    AuthenticationServiceResult getUserInfo(AuthenticationData authData) ;

    AuthenticationServiceResult authentication(AuthenticationData authData) ;
}
