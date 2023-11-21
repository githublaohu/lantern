package com.lamp.lantern.plugins.api.auth;

/**
 * 提供权限认证服务
 * @author laohu
 */
public interface AuthenticationService {

    AuthenticationServiceResult getUserInfo(AuthenticationData authData) ;

    AuthenticationServiceResult authentication(AuthenticationData authData) ;
}
