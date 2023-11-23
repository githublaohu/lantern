package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationRpcService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.config.DubboAuthenticationConfig;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.util.Objects;

/**
 * @author laohu
 */
public class DubboAuthenticationService implements AuthenticationService {

    private AuthenticationRpcService authenticationRpcService;

    public DubboAuthenticationService(DubboAuthenticationConfig config) {
        init(config);
    }

    void init(DubboAuthenticationConfig config) {
        ReferenceConfig<AuthenticationRpcService> referenceConfig = new ReferenceConfig<AuthenticationRpcService>();
        referenceConfig.setApplication(new ApplicationConfig(config.getApplicationName()));
        if (Objects.nonNull(config.getUrl())) {
            referenceConfig.setUrl(config.getUrl());
        }
        else{
            referenceConfig.setRegistry(new RegistryConfig(config.getRegistryAddress()));
        }
        referenceConfig.setInterface(AuthenticationService.class);
        referenceConfig.setTimeout(config.getTimeout());
        referenceConfig.setGroup(config.getGroup());
        referenceConfig.setVersion(config.getVersion());
        authenticationRpcService = referenceConfig.get();
    }

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        return authenticationRpcService.getUserInfo(authData);
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        return authenticationRpcService.authentication(authData);
    }
}
