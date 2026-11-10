package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicy;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicy.Mode;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.AuthenticationType;
import com.lamp.lantern.plugins.api.auth.LanternAuthCachePool;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationDataConfig;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import com.lamp.lantern.plugins.api.auth.config.DubboAuthenticationConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationFlowConfig;
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
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 1. springmvc参数拦截器
 * 2. springboot启动器
 * 用上下文传递UserInfo
 *
 * @author laohu
 */
@Component
public class AuthenticationManager implements AutoCloseable {

    private final LanternAuthFlow lanternAuthFlow;
    private final AuthenticationConfig authenticationConfig;
    private final EnvironmentContext environmentContext;
    private final AuthenticationPolicyService policyService;
    private final Long systemId;
    private final InjvmAuthenticationServices injvmAuthenticationServices;
    private LocalAuthenticationService localAuthenticationService;

    @Getter
    private AuthenticationService authenticationService;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;


    public AuthenticationManager(AuthenticationConfig authenticationConfig, EnvironmentContext environmentContext)
            throws Exception {
        Assert.notNull(authenticationConfig, "Lantern authenticationConfig is required");
        this.environmentContext = environmentContext;
        this.authenticationConfig = authenticationConfig;
        AuthenticationFlowConfig flowConfig = authenticationConfig.getAuthenticationFlowConfig();
        Assert.notNull(flowConfig, "Lantern authenticationFlowConfig is required");
        Assert.isTrue("header".equals(flowConfig.getTokenSpot()) || "cookie".equals(flowConfig.getTokenSpot()),
                "Lantern tokenSpot must be header or cookie");
        Assert.hasText(flowConfig.getTokenName(), "Lantern tokenName is required");
        this.lanternAuthFlow = new LanternAuthFlow(flowConfig);
        String dubboLocalServiceName = authenticationConfig.getDubboLocalServiceName();
        boolean useInjvm = StringUtils.hasText(dubboLocalServiceName);
        if (useInjvm || StringUtils.hasText(flowConfig.getPolicyServiceName())) {
            Assert.hasText(flowConfig.getSystemId(), "Lantern systemId is required with a policy service");
            this.systemId = Long.valueOf(flowConfig.getSystemId());
            Assert.isTrue(systemId > 0, "Lantern systemId must be positive");
            Assert.isTrue(CollectionUtils.isEmpty(flowConfig.getNotAuthentication())
                    && CollectionUtils.isEmpty(flowConfig.getUserAuthentication()),
                    "Lantern database policies and YAML path rules cannot be enabled together");
        } else {
            Assert.notNull(authenticationConfig.getAuthenticationType(), "Lantern authenticationType is required without a policy service");
            this.systemId = null;
        }
        if (useInjvm) {
            Assert.isTrue(!StringUtils.hasText(flowConfig.getPolicyServiceName())
                    && authenticationConfig.getDefaultAuthenticationService() == null
                    && authenticationConfig.getAuthenticationServiceConfig() == null
                    && authenticationConfig.getUserInfoAuthenticationServiceConfig() == null,
                    "Lantern dubboLocalServiceName cannot be combined with other authentication or policy services");
            Object implementation = environmentContext.getBean(dubboLocalServiceName);
            Assert.isInstanceOf(AuthenticationService.class, implementation, "Lantern Dubbo local bean must implement AuthenticationService");
            Assert.isInstanceOf(AuthenticationPolicyService.class, implementation, "Lantern Dubbo local bean must implement AuthenticationPolicyService");
            this.injvmAuthenticationServices = new InjvmAuthenticationServices(
                    (AuthenticationService) implementation, (AuthenticationPolicyService) implementation);
            this.authenticationService = injvmAuthenticationServices.getAuthenticationService();
            this.policyService = injvmAuthenticationServices.getPolicyService();
        } else {
            this.injvmAuthenticationServices = null;
            this.policyService = systemId == null ? null
                    : (AuthenticationPolicyService) environmentContext.getBean(flowConfig.getPolicyServiceName());
            this.initAuthenticationService();
            if (this.localAuthenticationService != null) {
                this.startCacheSync();
            }
        }
    }


    private void initAuthenticationService() throws Exception {
        AuthenticationService defaultService = this.createAuthenticationService(authenticationConfig.getDefaultAuthenticationService());
        AuthenticationService permissionService = defaultService;
        AuthenticationService identityService = defaultService;
        if (defaultService == null) {
            permissionService = this.createAuthenticationService(authenticationConfig.getAuthenticationServiceConfig());
            identityService = authenticationConfig.getUserInfoAuthenticationServiceConfig() == null
                    ? permissionService : this.createAuthenticationService(authenticationConfig.getUserInfoAuthenticationServiceConfig());
        }
        Assert.notNull(identityService, "Lantern identity service is required");
        if (policyService != null || authenticationConfig.getAuthenticationType() == AuthenticationType.RESOURCE) {
            Assert.notNull(permissionService, "Lantern resource authorization service is required");
        }
        ProxyAuthenticationService proxy = new ProxyAuthenticationService();
        proxy.setAuthenticationService(permissionService);
        proxy.setUserInfoService(identityService);
        this.authenticationService = proxy;
    }

    private AuthenticationDataService createAuthenticationDataService() throws Exception {
        AuthenticationDataConfig authenticationDataConfig = this.authenticationConfig.getAuthenticationDataConfig();
        Assert.notNull(authenticationDataConfig, "Lantern local cache requires authenticationDataConfig");
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

        Assert.notNull(authenticationDataService, "Lantern cache data service is required");
        return authenticationDataService;
    }

    private void startCacheSync() throws Exception {
        AuthenticationDataService dataService = this.createAuthenticationDataService();
        Long interval = authenticationConfig.getAuthenticationDataConfig().getDataSyncInterval();
        Assert.isTrue(interval != null && interval > 0, "Lantern cache dataSyncInterval must be positive");
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.schedule(() -> {
            AuthenticationData data = new AuthenticationData();
            data.setTime(0L);
            LanternAuthCachePool cachePool = dataService.getLanternAuthCachePool(data);
            if (cachePool != null) {
                localAuthenticationService.setLanternAuthCachePool(cachePool);
            }
        }, interval, TimeUnit.MICROSECONDS);
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
        return this.authentication(request, response, policyService == null ? lanternAuthFlow.getResource(request) : null);
    }

    public AuthenticationServiceResult authentication(HttpServletRequest request, HttpServletResponse response, String route) {
        AuthenticationPolicy policy = this.findPolicy(request.getMethod(), route);
        if (policy == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return new AuthenticationServiceResult().setSuccess(false);
        }
        Mode mode = Objects.requireNonNull(policy.getMode(), "Lantern policy mode is required");
        if (mode == Mode.PUBLIC) {
            return new AuthenticationServiceResult().setSuccess(true);
        }

        String token = lanternAuthFlow.getToken(request);
        if (Objects.isNull(token)) {
            lanternAuthFlow.failed(response);
            return new AuthenticationServiceResult().setSuccess(false);
        }
        AuthenticationData data = new AuthenticationData();
        data.setToken(token);
        data.setSystemId(systemId);
        data.setResource(route);
        data.setResourceId(policy.getResourceId());
        AuthenticationServiceResult identity = authenticationService.getUserInfo(data);
        if (!identity.isSuccess() || identity.getUserInfo() == null || identity.getUserInfo().getUiId() == null
                || (systemId != null && !systemId.equals(identity.getUserInfo().getSystemId()))) {
            lanternAuthFlow.failed(response);
            return new AuthenticationServiceResult().setSuccess(false);
        }
        if (mode == Mode.USER) {
            return identity;
        }

        data.setUserId(identity.getUserInfo().getUiId());
        data.setUserInfo(identity.getUserInfo());
        AuthenticationServiceResult result = authenticationService.authentication(data);
        if (!result.isSuccess()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            result.setUserInfo(identity.getUserInfo());
        }
        return result;
    }

    private AuthenticationPolicy findPolicy(String method, String route) {
        if (policyService != null) {
            return StringUtils.hasText(route) ? policyService.findPolicy(systemId, method, route) : null;
        }
        boolean publicPath = lanternAuthFlow.notAuthentication(route);
        boolean userPath = lanternAuthFlow.userAuthentication(route);
        if (publicPath && userPath) {
            throw new IllegalStateException("Conflicting Lantern YAML path rules");
        }
        if (publicPath) {
            return new AuthenticationPolicy(null, Mode.PUBLIC);
        }
        Mode mode = userPath || authenticationConfig.getAuthenticationType() == AuthenticationType.USER ? Mode.USER : Mode.ROLE;
        return new AuthenticationPolicy(null, mode);
    }

    @Override
    public void close() {
        if (injvmAuthenticationServices != null) {
            injvmAuthenticationServices.close();
        }
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdownNow();
        }
    }
}
