package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import java.util.Collections;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.FrameworkModel;
import org.apache.dubbo.rpc.model.ModuleModel;

final class InjvmAuthenticationServices implements AutoCloseable {

    private static final String APPLICATION_NAME = "lantern-authentication";
    private static final String PROTOCOL = "injvm";
    private static final String SERIALIZATION = "fastjson2";

    private final FrameworkModel framework;
    private final AuthenticationService authenticationService;
    private final AuthenticationPolicyService policyService;

    InjvmAuthenticationServices(AuthenticationService authentication, AuthenticationPolicyService policy) throws Exception {
        framework = new FrameworkModel();
        try {
            ApplicationModel model = framework.newApplication();
            ApplicationConfig application = new ApplicationConfig(APPLICATION_NAME);
            application.setQosEnable(false);
            application.setMetadataServiceProtocol(PROTOCOL);
            model.getApplicationConfigManager().setApplication(application);
            ModuleModel module = model.getDefaultModule();

            registerProvider(module, AuthenticationService.class, authentication);
            registerProvider(module, AuthenticationPolicyService.class, policy);
            ReferenceConfig<AuthenticationService> authenticationReference = registerReference(module, AuthenticationService.class);
            ReferenceConfig<AuthenticationPolicyService> policyReference = registerReference(module, AuthenticationPolicyService.class);
            module.getDeployer().start().get();
            authenticationService = authenticationReference.get();
            policyService = policyReference.get();
        } catch (Exception | Error failure) {
            try {
                close();
            } catch (RuntimeException | Error cleanupFailure) {
                failure.addSuppressed(cleanupFailure);
            }
            if (failure instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw failure;
        }
    }

    AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    AuthenticationPolicyService getPolicyService() {
        return policyService;
    }

    private <T> void registerProvider(ModuleModel module, Class<T> serviceInterface, T implementation) {
        ServiceConfig<T> service = new ServiceConfig<>(module);
        service.setRegistry(new RegistryConfig(RegistryConfig.NO_AVAILABLE));
        ProtocolConfig protocol = new ProtocolConfig(PROTOCOL);
        protocol.setSerialization(SERIALIZATION);
        service.setProtocol(protocol);
        service.setScope("local");
        service.setGroup(APPLICATION_NAME);
        service.setInterface(serviceInterface);
        service.setRef(implementation);
        module.getConfigManager().addService(service);
    }

    private <T> ReferenceConfig<T> registerReference(ModuleModel module, Class<T> serviceInterface) {
        ReferenceConfig<T> reference = new ReferenceConfig<>(module);
        reference.setRegistry(new RegistryConfig(RegistryConfig.NO_AVAILABLE));
        reference.setScope("local");
        reference.setRetries(0);
        reference.setParameters(Collections.singletonMap("serialization", SERIALIZATION));
        reference.setGroup(APPLICATION_NAME);
        reference.setInterface(serviceInterface);
        module.getConfigManager().addReference(reference);
        return reference;
    }

    @Override
    public void close() {
        framework.destroy();
    }
}
