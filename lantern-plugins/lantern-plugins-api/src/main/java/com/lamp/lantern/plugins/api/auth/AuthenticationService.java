package com.lamp.lantern.plugins.api.auth;

/**
 * @author laohu
 */
public interface AuthenticationService {

    AuthenticationServiceResult getUserInfo(AuthenticationData authData) ;

    AuthenticationServiceResult authentication(AuthenticationData authData) ;
}
