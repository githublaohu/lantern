package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.auth.*;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.core.authentication.service.CacheAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.DubboAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.LocalAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.ProxyAuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 1. springmvc参数拦截器
 * 2. springboot启动器
 * 用上下文传递UserInfo
 *
 * @author laohu
 */
public class AuthenticationManager {

    private final LanternAuthFlow lanternAuthFlow = new LanternAuthFlow();

    private AuthenticationDataService authOperation;

    private AuthenticationService authenticationService;

    private AuthenticationConfig authenticationConfig;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;


    public AuthenticationManager(AuthenticationConfig authenticationConfig) {
        this.authOperation = authOperation;
        this.authenticationConfig = authenticationConfig;

        this.initAuthenticationService();
    }

    private void initAuthenticationService() {
        //不使用代理
        if (Objects.nonNull(this.authenticationConfig.getAuthenticationService())) {
            this.authenticationService = this.authenticationConfig.getAuthenticationService();
            return;
        }
        AuthenticationService authenticationService = this.getAuthenticationService(this.authenticationConfig.getAuthenticationServiceConfig());

        ProxyAuthenticationService proxyAuthenticationService = new ProxyAuthenticationService();
        proxyAuthenticationService.setAuthenticationService(authenticationService);
        proxyAuthenticationService.setUserInfoService(Objects.nonNull(authenticationService) ? authenticationService : this.getAuthenticationService(this.authenticationConfig.getUserInfoAuthenticationServiceConfig()));

        this.authenticationService = proxyAuthenticationService;

    }

    public AuthenticationService getAuthenticationService(AuthenticationServiceConfig authenticationServiceConfig) {
        AuthenticationService authenticationService = null;
        if (Objects.nonNull(authenticationServiceConfig.getAuthenticationService())) {
            authenticationService = authenticationServiceConfig.getAuthenticationService();
        } else if (Objects.nonNull(authenticationServiceConfig)) {
            authenticationService = authenticationServiceConfig.getAuthenticationService();
        } else if (Objects.nonNull(authenticationServiceConfig.getDubboAuthenticationConfig())) {
            // REDIS
            authenticationService = new DubboAuthenticationService(authenticationServiceConfig.getDubboAuthenticationConfig());
        } else if (Objects.nonNull(authenticationServiceConfig.getRedisCacheConfig())) {
            // REDIS
            authenticationService = new CacheAuthenticationService(authenticationServiceConfig.getRedisCacheConfig());
        }
        if (Objects.nonNull(authenticationServiceConfig.getLocalCacheConfig())) {
            //
            LocalAuthenticationService localAuthenticationService = new LocalAuthenticationService();
            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            AuthenticationData authenticationData = new AuthenticationData();

            scheduledThreadPoolExecutor.schedule(() -> {
                LanternAuthCachePool lanternAuthCachePool = authenticationServiceConfig.getLocalCacheConfig().getAuthOperation().getLanternAuthCachePool(authenticationData);
                localAuthenticationService.setLanternAuthCachePool(lanternAuthCachePool);
            }, authenticationServiceConfig.getLocalCacheConfig().getDataSyncInterval(), TimeUnit.MICROSECONDS);
            authenticationService = localAuthenticationService;

        }
        return authenticationService;
    }


    public AuthenticationServiceResult authentication(HttpServletRequest request, HttpServletResponse response) {

        String resource = lanternAuthFlow.getResource(request);

        if (lanternAuthFlow.notAuthentication(resource)) {
            return new AuthenticationServiceResult().setSuccess(true);
        }

        String token = lanternAuthFlow.getToken(request);
        if (Objects.isNull(token)) {
            lanternAuthFlow.failed(response);
            return new AuthenticationServiceResult().setSuccess(false);
        }
        AuthenticationData authenticationData = new AuthenticationData();
        if (Objects.equals(authenticationConfig.getAuthenticationType(), AuthenticationType.USER)) {
            return authenticationService.getUserInfo(authenticationData);
        }
        //AuthenticationType.RESOURCE
        AuthenticationServiceResult authenticationServiceResult =
                authenticationService.authentication(authenticationData);

        if (!authenticationServiceResult.isSuccess()) {
            lanternAuthFlow.failed(response);
        }
        return authenticationServiceResult;
    }


}
