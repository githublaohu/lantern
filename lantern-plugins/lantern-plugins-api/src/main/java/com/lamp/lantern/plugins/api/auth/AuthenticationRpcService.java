package com.lamp.lantern.plugins.api.auth;

/**
 * @author hahaha
 */
public interface AuthenticationRpcService {
    AuthenticationServiceResult getUserInfo(AuthenticationData authData) ;

    AuthenticationServiceResult authentication(AuthenticationData authData) ;
}
