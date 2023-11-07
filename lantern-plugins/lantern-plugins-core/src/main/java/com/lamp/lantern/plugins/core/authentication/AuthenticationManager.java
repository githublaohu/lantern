package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.auth.*;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.core.authentication.service.DubboAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.ProxyAuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author laohu
 */
public class AuthenticationManager {

    private final LanternAuthFlow lanternAuthFlow = new LanternAuthFlow();

    private AuthenticationDataService authOperation;

    private AuthenticationService authenticationService;

    private AuthenticationConfig authenticationConfig;


    public AuthenticationManager(AuthenticationDataService authOperation,
                                 AuthenticationService authenticationService,
                                 AuthenticationConfig authenticationConfig) {
        this.authOperation = authOperation;
        this.authenticationService = authenticationService;
        this.authenticationConfig = authenticationConfig;
    }

    private void initAuthenticationService() {
        //不使用代理
        if (Objects.nonNull(this.authenticationConfig.getAuthenticationService())) {
            this.authenticationService = this.authenticationConfig.getAuthenticationService();
            return;
        }
        AuthenticationServiceConfig authenticationServiceConfig = authenticationConfig.getAuthenticationServiceConfig();

        //使用代理
        AuthenticationService authenticationService = null;
        if (Objects.nonNull(authenticationServiceConfig.getAuthenticationService())) {
            authenticationService = authenticationServiceConfig.getAuthenticationService();
        }

        AuthenticationService userInfoAuthenticationService = null;
        authenticationServiceConfig = authenticationConfig.getUserInfoAuthenticationServiceConfig();
        if (Objects.nonNull(authenticationServiceConfig)) {
            userInfoAuthenticationService = authenticationServiceConfig.getAuthenticationService();
        } else {
            // REDIS
            userInfoAuthenticationService = new DubboAuthenticationService(authenticationServiceConfig.getDubboAuthenticationConfig());


        }


        ProxyAuthenticationService proxyAuthenticationService = new ProxyAuthenticationService();
        proxyAuthenticationService.setAuthenticationService(authenticationService);
        proxyAuthenticationService.setUserInfoService(userInfoAuthenticationService);

        this.authenticationService = proxyAuthenticationService;

    }

    public boolean authentication(HttpServletRequest request, HttpServletResponse response) {

        String resource = lanternAuthFlow.getResource(request);

        if (lanternAuthFlow.notAuthentication(resource)) {
            return true;
        }

        String token = lanternAuthFlow.getToken(request);
        if (Objects.isNull(token)) {
            lanternAuthFlow.failed(response);
            return false;
        }
        AuthenticationData authenticationData = new AuthenticationData();
        if (Objects.equals(authenticationConfig.getAuthenticationType(), AuthenticationType.USER)) {
            authenticationService.getUserInfo(authenticationData);
            return true;
        }
        //AuthenticationType.RESOURCE
        AuthenticationServiceResult authenticationServiceResult =
                authenticationService.authentication(authenticationData);
        if (authenticationServiceResult.isSuccess()) {


        } else {
            lanternAuthFlow.failed(response);
        }
        return authenticationServiceResult.isSuccess();

    }


}
