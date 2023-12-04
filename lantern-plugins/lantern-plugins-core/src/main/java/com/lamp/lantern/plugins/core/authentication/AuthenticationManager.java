package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.AuthenticationType;
import com.lamp.lantern.plugins.api.auth.LanternAuthCachePool;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationDataConfig;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import com.lamp.lantern.plugins.api.auth.config.DubboAuthenticationConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.core.authentication.service.CacheAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.DubboAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.LocalAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.ProxyAuthenticationService;
import com.lamp.lantern.plugins.core.environment.EnvironmentContext;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 1. springmvc参数拦截器
 * 2. springboot启动器
 * 用上下文传递UserInfo
 *
 * @author laohu
 */
@Component
public class AuthenticationManager {

    private final LanternAuthFlow lanternAuthFlow = new LanternAuthFlow();
    private final AuthenticationConfig authenticationConfig;
    private final EnvironmentContext environmentContext;
    private final AuthenticationDataService authenticationDataService;
    private LocalAuthenticationService localAuthenticationService;

    @Getter
    private AuthenticationService authenticationService;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;


    public AuthenticationManager(AuthenticationConfig authenticationConfig, EnvironmentContext environmentContext)
            throws Exception {
        this.environmentContext = environmentContext;
        this.authenticationConfig = authenticationConfig;
        this.authenticationDataService = this.createAuthenticationDataService();
        this.initAuthenticationService();
    }


    private void initAuthenticationService() throws Exception {
        ProxyAuthenticationService proxyAuthenticationService = new ProxyAuthenticationService();
        if (Objects.nonNull(this.authenticationConfig.getDefaultAuthenticationService().getAuthenticationService())) {
            this.authenticationService =
                    this.createAuthenticationService(authenticationConfig.getDefaultAuthenticationService());
            proxyAuthenticationService.setAuthenticationService(authenticationService);
            proxyAuthenticationService.setUserInfoService(authenticationService);
        } else {
            AuthenticationService authenticationService =
                    this.createAuthenticationService(this.authenticationConfig.getAuthenticationServiceConfig());
            proxyAuthenticationService.setAuthenticationService(authenticationService);
            proxyAuthenticationService.setUserInfoService(
                    Objects.nonNull(authenticationService) ? authenticationService : this.createAuthenticationService(
                            this.authenticationConfig.getUserInfoAuthenticationServiceConfig()));
        }
        this.authenticationService = proxyAuthenticationService;

    }

    private AuthenticationDataService createAuthenticationDataService() throws Exception {
        AuthenticationDataConfig authenticationDataConfig = this.authenticationConfig.getAuthenticationDataConfig();
        AuthenticationDataService authenticationDataService = null;
        if (Objects.nonNull(authenticationDataConfig.getAuthenticationDataService())) {
            authenticationDataService = authenticationDataConfig.getAuthenticationDataService();
        } else if (Objects.nonNull(authenticationDataConfig.getAuthenticationServiceName())) {
            authenticationDataService = (AuthenticationDataService) environmentContext.getBean(
                    authenticationDataConfig.getAuthenticationServiceName());
        } else if (Objects.nonNull(authenticationDataConfig.getDubboAuthenticationConfig())) {
            DubboAuthenticationConfig dubboAuthenticationConfig =
                    authenticationDataConfig.getDubboAuthenticationConfig();
            ReferenceConfig<AuthenticationDataService> referenceConfig = new ReferenceConfig<>();
            referenceConfig.setApplication(new ApplicationConfig(dubboAuthenticationConfig.getApplicationName()));
            if (Objects.nonNull(dubboAuthenticationConfig.getUrl())) {
                referenceConfig.setUrl(dubboAuthenticationConfig.getUrl());
            } else {
                referenceConfig.setRegistry(new RegistryConfig(dubboAuthenticationConfig.getRegistryAddress()));
            }
            referenceConfig.setInterface(AuthenticationDataService.class);
            referenceConfig.setTimeout(dubboAuthenticationConfig.getTimeout());
            referenceConfig.setGroup(dubboAuthenticationConfig.getGroup());
            referenceConfig.setVersion(dubboAuthenticationConfig.getVersion());
            authenticationDataService = referenceConfig.get();
        }

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        AuthenticationData authenticationData = new AuthenticationData();

        scheduledThreadPoolExecutor.schedule(() -> {
            LanternAuthCachePool lanternAuthCachePool =
                    this.authenticationDataService.getLanternAuthCachePool(authenticationData);
            localAuthenticationService.setLanternAuthCachePool(lanternAuthCachePool);
        }, authenticationDataConfig.getDataSyncInterval(), TimeUnit.MICROSECONDS);

        if (Objects.isNull(authenticationDataService)) {
            throw new RuntimeException("");
        }

        return authenticationDataService;
    }

    private AuthenticationService createAuthenticationService(AuthenticationServiceConfig authenticationServiceConfig)
            throws Exception {
        if (Objects.isNull(authenticationServiceConfig)) {
            return null;
        }
        AuthenticationService authenticationService = null;
        if (Objects.nonNull(authenticationServiceConfig.getAuthenticationService())) {
            authenticationService = authenticationServiceConfig.getAuthenticationService();
        } else if (Objects.nonNull(authenticationServiceConfig.getAuthenticationServiceName())) {
            authenticationService = (AuthenticationService) environmentContext.getBean(
                    authenticationServiceConfig.getAuthenticationServiceName());
        } else if (Objects.nonNull(authenticationServiceConfig.getDubboAuthenticationConfig())) {
            // REDIS
            authenticationService =
                    new DubboAuthenticationService(authenticationServiceConfig.getDubboAuthenticationConfig());
        } else if (Objects.nonNull(authenticationServiceConfig.getRedisCacheConfig())) {
            // REDIS
            authenticationService = new CacheAuthenticationService(authenticationServiceConfig.getRedisCacheConfig());
        }
        if (Objects.nonNull(authenticationServiceConfig.getLocalCacheConfig())) {
            //
            localAuthenticationService = new LocalAuthenticationService();

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
