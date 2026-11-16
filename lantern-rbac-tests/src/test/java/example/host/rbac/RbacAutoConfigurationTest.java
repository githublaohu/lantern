package example.host.rbac;

import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.authentication.service.CacheAuthenticationService;
import com.lamp.lantern.plugins.core.authentication.service.LocalAuthenticationService;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatClient;
import com.lamp.lantern.plugins.springmvc.support.LanternEnterpriseWechatController;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.springmvc.autoconfigure.LanternProperties;
import com.lamp.lantern.plugins.springmvc.support.AuthenticationInterceptor;
import com.lamp.lantern.plugins.springmvc.support.LanternSpringMVCBehavior;
import com.lamp.lantern.plugins.springmvc.support.LanternDatabaseLoginController;
import example.host.HostProbeController;
import example.host.RbacHost;
import example.host.mapper.BusinessMapper;
import javax.sql.DataSource;
import org.apache.dubbo.config.spring.reference.ReferenceBeanManager;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.rpc.cluster.ClusterInvoker;
import org.apache.dubbo.rpc.service.EchoService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RbacAutoConfigurationTest {

    private WebApplicationContextRunner host() {
        return new WebApplicationContextRunner().withUserConfiguration(RbacHost.class)
                .withPropertyValues("spring.sql.init.mode=never");
    }

    private WebApplicationContextRunner local() {
        return host().withPropertyValues("lantern.authenticationConfig.enable=true",
                "lantern.authenticationConfig.authenticationFlowConfig.systemId=915",
                "lantern.authenticationConfig.authenticationFlowConfig.includePaths[0]=" + HostProbeController.CLUSTER_ROUTE);
    }

    @Test
    void enterpriseWechatIsOptInAndUsesConfigurationOnly() {
        local().run(context -> assertThat(context).hasNotFailed()
                .doesNotHaveBean(EnterpriseWechatClient.class).doesNotHaveBean(LanternEnterpriseWechatController.class));
        local().withUserConfiguration(EventMeshScan.class)
                .withPropertyValues("lantern.loginConfig.enterpriseWechat.enabled=true",
                "lantern.loginConfig.enterpriseWechat.corpId=ww-test-corp",
                "lantern.loginConfig.enterpriseWechat.agentId=1000002",
                "lantern.loginConfig.enterpriseWechat.secret=test-secret-only",
                "lantern.loginConfig.enterpriseWechat.redirectUri=https://dashboard.example/account/enterprise-wechat/callback")
                .run(context -> assertThat(context).hasNotFailed().hasSingleBean(EnterpriseWechatClient.class)
                        .hasSingleBean(LanternEnterpriseWechatController.class).doesNotHaveBean(HandlerService.class)
                        .doesNotHaveBean(RedisConnectionFactory.class));
    }

    @Test
    void enterpriseWechatAlsoBindsStandardKebabCaseProperties() {
        local().withPropertyValues("lantern.login-config.enterprise-wechat.enabled=true",
                "lantern.login-config.enterprise-wechat.corp-id=ww-test-corp",
                "lantern.login-config.enterprise-wechat.agent-id=1000002",
                "lantern.login-config.enterprise-wechat.secret=test-secret-only",
                "lantern.login-config.enterprise-wechat.redirect-uri=https://dashboard.example/account/enterprise-wechat/callback")
                .run(context -> assertThat(context).hasNotFailed().hasSingleBean(EnterpriseWechatClient.class)
                        .hasSingleBean(LanternEnterpriseWechatController.class));
    }

    @Test
    void enabledEnterpriseWechatWithMissingCredentialsFailsAtStartup() {
        local().withPropertyValues("lantern.loginConfig.enterpriseWechat.enabled=true").run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure()).hasStackTraceContaining("corpId is required");
        });
    }

    @Test
    void disabledAuthenticationDoesNotCreateServicesOrMvcExtensions() {
        host().withPropertyValues("server.port=9898", "lantern.authenticationConfig.enable=false").run(context -> {
            assertThat(context).hasNotFailed().doesNotHaveBean(AuthenticationManager.class)
                    .doesNotHaveBean(AuthenticationInterceptor.class).doesNotHaveBean(ReferenceBeanManager.class)
                    .doesNotHaveBean(HandlerService.class).doesNotHaveBean(LanternSpringMVCBehavior.class)
                    .doesNotHaveBean("DBAuthentication");
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context.getSourceApplicationContext()).build();
            mvc.perform(post("/public/echo").contentType(MediaType.APPLICATION_JSON).content("{\"message\":\"ok\"}"))
                    .andExpect(status().isOk()).andExpect(content().json("{\"message\":\"ok\"}"));
        });
    }

    @Test
    void dependencyAndConfigurationAloneCreateInjvmServicesSharingBusinessMybatis() {
        local().withPropertyValues("dubbo.application.name=host-custom-name",
                "dubbo.provider.group=host-custom-group", "dubbo.consumer.group=host-custom-group",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration")
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(LanternProperties.class)
                            .hasSingleBean(DataSource.class).hasSingleBean(SqlSessionFactory.class)
                            .doesNotHaveBean(RedisConnectionFactory.class).doesNotHaveBean(HandlerService.class)
                            .doesNotHaveBean(CacheAuthenticationService.class).doesNotHaveBean(LocalAuthenticationService.class)
                            .doesNotHaveBean(AuthenticationDataService.class);
                    assertThat(context).hasSingleBean(LanternDatabaseLoginController.class).hasBean("lanternDatabaseLogin");
                    assertThat(context.getEnvironment().getProperty("spring.autoconfigure.exclude"))
                            .isEqualTo("org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration");
                    assertThat(context.getEnvironment().getProperty("dubbo.application.name")).isEqualTo("host-custom-name");
                    SqlSessionFactory business = context.getBean(SqlSessionFactory.class);
                    assertThat(business.getConfiguration().getEnvironment().getDataSource())
                            .isSameAs(context.getBean(DataSource.class));
                    assertThat(context.getBean(BusinessMapper.class).ping()).isEqualTo(1);
                    for (String name : new String[]{"businessMapper", "userInfoMapper", "roleMapper", "resourcesMapper"}) {
                        assertThat(context.getBean("&" + name, MapperFactoryBean.class).getSqlSessionFactory()).isSameAs(business);
                    }
                    assertThat(ReflectionTestUtils.getField(context.getBean(AuthenticationManager.class),
                            "scheduledThreadPoolExecutor")).isNull();
                    AuthenticationService authentication = context.getBean("lanternDubboAuthentication", AuthenticationService.class);
                    AuthenticationPolicyService policy = context.getBean("lanternDubboPolicy", AuthenticationPolicyService.class);
                    assertThat(context.getBean(AuthenticationManager.class).getAuthenticationService()).isSameAs(authentication);
                    assertThat(ReflectionTestUtils.getField(context.getBean(AuthenticationManager.class), "policyService"))
                            .isSameAs(policy);
                    assertThat(context.getBean("&lanternDubboAuthentication", ReferenceBean.class).getObject()).isSameAs(authentication);
                    assertThat(context.getBean("&lanternDubboPolicy", ReferenceBean.class).getObject()).isSameAs(policy);
                    ReferenceBeanManager references = context.getBean(ReferenceBeanManager.class);
                    assertThat(references.getReferences()).extracting(ref -> ref.getInterfaceClass())
                            .containsExactlyInAnyOrder(AuthenticationService.class, AuthenticationPolicyService.class);
                    references.getReferences().forEach(ref -> {
                        assertThat(((EchoService) ref.getObject()).$echo("injvm")).isEqualTo("injvm");
                        assertThat(ref.getReferenceConfig().getScope()).isEqualTo("local");
                        assertThat(ref.getReferenceConfig().getGroup()).isEqualTo("host-custom-group");
                        ClusterInvoker<?> invoker = (ClusterInvoker<?>) ref.getReferenceConfig().getInvoker();
                        assertThat(invoker.getDirectory().getAllInvokers()).isNotEmpty()
                                .allSatisfy(local -> assertThat(local.getUrl().getProtocol()).isEqualTo("injvm"));
                    });
                });
    }

    @Test
    void eventmeshStyleComponentScanningDoesNotRegisterDuplicateBeans() {
        local().withUserConfiguration(EventMeshScan.class).run(context -> {
            assertThat(context).hasNotFailed().hasSingleBean(LanternProperties.class)
                    .hasSingleBean(AuthenticationManager.class).hasSingleBean(AuthenticationInterceptor.class);
            assertThat(context.getBeansOfType(DataSource.class)).containsOnlyKeys("businessDataSource");
        });
    }

    @Test
    void remoteScopeWithoutRegistryFailsInsteadOfSilentlyCallingLocalService() {
        local().withPropertyValues("dubbo.consumer.scope=remote").run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure()).hasStackTraceContaining("No such any registry to reference");
        });
    }

    @Test
    void databaseLoginCanBeDisabledWithoutDisablingAuthorization() {
        local().withPropertyValues("lantern.loginConfig.enable=false").run(context -> {
            assertThat(context).hasNotFailed().hasSingleBean(AuthenticationManager.class)
                    .doesNotHaveBean(LanternDatabaseLoginController.class).doesNotHaveBean("lanternDatabaseLogin")
                    .doesNotHaveBean(HandlerService.class);
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context.getSourceApplicationContext()).build();
            mvc.perform(post("/account/first/accountLogin").contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isNotFound());
        });
    }

    @Test
    void loginAndLogoutRemainReachableWhenAllPathsAreProtected() {
        local().withPropertyValues("lantern.authenticationConfig.authenticationFlowConfig.includePaths[0]=/**").run(context -> {
            assertThat(context).hasNotFailed();
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context.getSourceApplicationContext()).build();
            mvc.perform(post("/account/first/accountLogin").contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isUnauthorized()).andExpect(header().doesNotExist("Authorization"));
            mvc.perform(post("/account/logout")).andExpect(status().isUnauthorized());
        });
    }

    @Test
    void customAuthenticationServiceBacksOffDatabaseAndPreservesBodyInjection() {
        AuthenticationService service = mock(AuthenticationService.class);
        when(service.getUserInfo(any())).thenReturn(RbacCoreTest.identity(7L, null));
        host().withBean("customAuthentication", AuthenticationService.class, () -> service)
                .withPropertyValues("lantern.authenticationConfig.enable=true",
                        "lantern.authenticationConfig.defaultAuthenticationService.authenticationServiceName=customAuthentication",
                        "lantern.authenticationConfig.authenticationType=USER",
                        "lantern.authenticationConfig.authenticationFlowConfig.tokenSpot=header",
                        "lantern.authenticationConfig.authenticationFlowConfig.tokenName=Authorization",
                        "lantern.authenticationConfig.authenticationFlowConfig.includePaths[0]=" + HostProbeController.CLUSTER_ROUTE)
                .run(context -> {
                    assertThat(context).hasNotFailed().doesNotHaveBean("DBAuthentication")
                            .doesNotHaveBean(ReferenceBeanManager.class);
                    assertThat(context.getEnvironment().getProperty("dubbo.protocol.name")).isNull();
                    MockMvc mvc = MockMvcBuilders.webAppContextSetup(context.getSourceApplicationContext()).build();
                    mvc.perform(post(HostProbeController.CLUSTER_ROUTE).contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"message\":\"ok\"}")).andExpect(status().isUnauthorized());
                    mvc.perform(post(HostProbeController.CLUSTER_ROUTE).header("Authorization", "Bearer custom-token")
                                    .contentType(MediaType.APPLICATION_JSON).content("{\"message\":\"ok\",\"userId\":999}"))
                            .andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(7))
                            .andExpect(jsonPath("$.message").value("ok"));
                    assertThat(LanternContext.getContext().getUserInfo()).isNull();
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {"spring.redis.host=127.0.0.1",
            "lantern.authenticationConfig.redisAutoConfigurationEnabled=true"})
    void explicitHostRedisConfigurationIsNotSilentlyDisabled(String setting) {
        local().withPropertyValues(setting).run(context -> {
            assertThat(context).hasNotFailed().hasSingleBean(RedisConnectionFactory.class)
                    .doesNotHaveBean(HandlerService.class).doesNotHaveBean(CacheAuthenticationService.class);
        });
    }

    @Test
    void missingProtectedPathsFailStartup() {
        host().withPropertyValues("lantern.authenticationConfig.enable=true",
                "lantern.authenticationConfig.authenticationFlowConfig.systemId=915").run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure()).hasStackTraceContaining("includePaths");
        });
    }

    @Test
    void missingSystemIdFailsStartup() {
        host().withPropertyValues("lantern.authenticationConfig.enable=true",
                "lantern.authenticationConfig.authenticationFlowConfig.includePaths[0]=/protected").run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure()).hasStackTraceContaining("systemId is required");
        });
    }

    @Configuration(proxyBeanMethods = false)
    @ComponentScan(basePackages = {"com.lamp.lantern.plugins.springmvc.autoconfigure",
            "com.lamp.lantern.service.core.provider.autoconfigure"})
    static class EventMeshScan {
    }
}
