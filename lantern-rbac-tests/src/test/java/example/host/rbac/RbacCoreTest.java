package example.host.rbac;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicy;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicy.Mode;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.AuthenticationType;
import com.lamp.lantern.plugins.api.auth.config.AuthenticationServiceConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import com.lamp.lantern.plugins.api.config.AuthenticationFlowConfig;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.authentication.LanternAuthFlow;
import com.lamp.lantern.plugins.core.environment.EnvironmentContext;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.springmvc.support.AuthenticationInterceptor;
import java.util.Collections;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.HandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RbacCoreTest {

    private final AuthenticationService service = mock(AuthenticationService.class);
    private final AuthenticationPolicyService policies = mock(AuthenticationPolicyService.class);
    private final EnvironmentContext environment = mock(EnvironmentContext.class);
    private AuthenticationConfig config;

    @BeforeEach
    void configure() throws Exception {
        config = new AuthenticationConfig();
        AuthenticationServiceConfig selected = new AuthenticationServiceConfig();
        selected.setAuthenticationService(service);
        config.setDefaultAuthenticationService(selected);
        config.setAuthenticationType(AuthenticationType.RESOURCE);
        AuthenticationFlowConfig flow = config.getAuthenticationFlowConfig();
        flow.setTokenSpot("header");
        flow.setTokenName("Authorization");
        flow.setSystemId("915");
        flow.setPolicyServiceName("policies");
        when(environment.getBean("policies")).thenReturn(policies);
        when(policies.findPolicy(915L, "POST", "/resource/{id}")).thenReturn(new AuthenticationPolicy(42L, Mode.ROLE));
        when(service.getUserInfo(any())).thenReturn(identity(7L, 915L));
        when(service.authentication(any())).thenReturn(new AuthenticationServiceResult().setSuccess(true));
    }

    @AfterEach
    void clear() {
        LanternContext.removeContext();
    }

    @Test
    void propagatesCredentialsResourceAndAuthenticatedUserWithoutStartingCacheSync() throws Exception {
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            AuthenticationServiceResult result = manager.authentication(request("Bearer token-abc"), response(), "/resource/{id}");
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getUserInfo().getUiId()).isEqualTo(7L);
            ArgumentCaptor<AuthenticationData> data = ArgumentCaptor.forClass(AuthenticationData.class);
            verify(service).authentication(data.capture());
            assertThat(data.getValue().getToken()).isEqualTo("token-abc");
            assertThat(data.getValue().getSystemId()).isEqualTo(915L);
            assertThat(data.getValue().getResourceId()).isEqualTo(42L);
            assertThat(data.getValue().getUserId()).isEqualTo(7L);
            assertThat(ReflectionTestUtils.getField(manager, "scheduledThreadPoolExecutor")).isNull();
        }
    }

    @Test
    void publicPolicySkipsAuthenticationAndPermissionCalls() throws Exception {
        when(policies.findPolicy(any(), any(), any())).thenReturn(new AuthenticationPolicy(42L, Mode.PUBLIC));
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            assertThat(manager.authentication(request(null), response(), "/resource/{id}").isSuccess()).isTrue();
            verifyNoInteractions(service);
        }
    }

    @Test
    void userPolicyOnlyRequiresIdentity() throws Exception {
        when(policies.findPolicy(any(), any(), any())).thenReturn(new AuthenticationPolicy(42L, Mode.USER));
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            assertThat(manager.authentication(request("Bearer token"), response(), "/resource/{id}").isSuccess()).isTrue();
            verify(service, never()).authentication(any());
        }
    }

    @Test
    void missingPolicyOrMissingRouteFailsClosed() throws Exception {
        when(policies.findPolicy(any(), any(), any())).thenReturn(null);
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            MockHttpServletResponse response = response();
            assertThat(manager.authentication(request(null), response, "/resource/{id}").isSuccess()).isFalse();
            assertThat(response.getStatus()).isEqualTo(403);
            assertThat(manager.authentication(request(null), response, null).isSuccess()).isFalse();
            verifyNoInteractions(service);
        }
    }

    @Test
    void malformedMissingAndDuplicateBearerHeadersAreRejected() throws Exception {
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            for (String value : new String[]{null, "", "raw-token", "Bearer ", "Bearer one,two", "Bearer one two"}) {
                MockHttpServletResponse response = response();
                assertThat(manager.authentication(request(value), response, "/resource/{id}").isSuccess()).isFalse();
                assertThat(response.getStatus()).isEqualTo(401);
                assertThat(response.getHeader("WWW-Authenticate")).isEqualTo("Bearer");
            }
            MockHttpServletRequest duplicate = request("Bearer one");
            duplicate.addHeader("Authorization", "Bearer two");
            assertThat(manager.authentication(duplicate, response(), "/resource/{id}").isSuccess()).isFalse();
            verifyNoInteractions(service);
        }
    }

    @Test
    void wrongSystemMissingUserAndNullIdentityCannotAuthorize() throws Exception {
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            when(service.getUserInfo(any())).thenReturn(identity(7L, 916L), identity(null, 915L), null);
            for (int i = 0; i < 3; i++) {
                MockHttpServletResponse response = response();
                assertThat(manager.authentication(request("Bearer token"), response, "/resource/{id}").isSuccess()).isFalse();
                assertThat(response.getStatus()).isEqualTo(401);
            }
            verify(service, never()).authentication(any());
        }
    }

    @Test
    void authenticatedUserWithoutPermissionReceives403() throws Exception {
        when(service.authentication(any())).thenReturn(new AuthenticationServiceResult().setSuccess(false));
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            MockHttpServletResponse response = response();
            assertThat(manager.authentication(request("Bearer token"), response, "/resource/{id}").isSuccess()).isFalse();
            assertThat(response.getStatus()).isEqualTo(403);
        }
    }

    @Test
    void interceptorClearsContextForCompletionAsyncFailureAndNextRequest() throws Exception {
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(manager);
            MockHttpServletRequest request = request("Bearer token");
            MockHttpServletResponse response = response();
            assertThat(interceptor.preHandle(request, response, this)).isTrue();
            assertThat(LanternContext.getContext().getUserInfo()).isNotNull();
            interceptor.afterCompletion(request, response, this, null);
            assertThat(LanternContext.getContext().getUserInfo()).isNull();

            assertThat(interceptor.preHandle(request, response, this)).isTrue();
            interceptor.afterConcurrentHandlingStarted(request, response, this);
            assertThat(LanternContext.getContext().getUserInfo()).isNull();

            assertThat(interceptor.preHandle(request, response, this)).isTrue();
            assertThat(interceptor.preHandle(request(null), response(), this)).isFalse();
            assertThat(LanternContext.getContext().getUserInfo()).isNull();

            when(policies.findPolicy(any(), any(), any())).thenThrow(new IllegalStateException("database unavailable"));
            MockHttpServletResponse failure = response();
            assertThat(interceptor.preHandle(request, failure, this)).isFalse();
            assertThat(failure.getStatus()).isEqualTo(503);
            assertThat(LanternContext.getContext().getUserInfo()).isNull();
        }
    }

    @Test
    void legacyYamlPathsAndSeparateIdentityAndAuthorizationServicesStillWork() throws Exception {
        config.getAuthenticationFlowConfig().setPolicyServiceName(null);
        config.getAuthenticationFlowConfig().setNotAuthentication(Collections.singleton("/public"));
        config.setDefaultAuthenticationService(null);
        AuthenticationService permission = mock(AuthenticationService.class);
        when(permission.authentication(any())).thenReturn(new AuthenticationServiceResult().setSuccess(true));
        AuthenticationServiceConfig identityConfig = new AuthenticationServiceConfig();
        identityConfig.setAuthenticationService(service);
        AuthenticationServiceConfig permissionConfig = new AuthenticationServiceConfig();
        permissionConfig.setAuthenticationService(permission);
        config.setAuthenticationServiceConfig(permissionConfig);
        config.setUserInfoAuthenticationServiceConfig(identityConfig);
        try (AuthenticationManager manager = new AuthenticationManager(config, environment)) {
            MockHttpServletRequest publicRequest = request(null);
            publicRequest.setRequestURI("/public");
            assertThat(manager.authentication(publicRequest, response()).isSuccess()).isTrue();
            assertThat(manager.authentication(request("Bearer token"), response()).isSuccess()).isTrue();
            verify(permission).authentication(any());
            verify(service, never()).authentication(any());
        }
    }

    @Test
    void cookieModeHandlesMissingAndDuplicateCookies() {
        AuthenticationFlowConfig flow = new AuthenticationFlowConfig();
        flow.setTokenSpot("cookie");
        flow.setTokenName("access-token");
        LanternAuthFlow parser = new LanternAuthFlow(flow);
        MockHttpServletRequest request = request(null);
        assertThat(parser.getToken(request)).isNull();
        request.setCookies(new Cookie("access-token", "one"));
        assertThat(parser.getToken(request)).isEqualTo("one");
        request.setCookies(new Cookie("access-token", "one"), new Cookie("access-token", "two"));
        assertThat(parser.getToken(request)).isNull();
    }

    @Test
    void conflictingDatabaseAndYamlRulesFailAtStartup() {
        config.getAuthenticationFlowConfig().setNotAuthentication(Collections.singleton("/public"));
        assertThatThrownBy(() -> new AuthenticationManager(config, environment))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("cannot be enabled together");
    }

    static AuthenticationServiceResult identity(Long userId, Long systemId) {
        UserInfo user = new UserInfo();
        user.setUiId(userId);
        user.setSystemId(systemId);
        AuthenticationServiceResult result = new AuthenticationServiceResult().setSuccess(true);
        result.setUserInfo(user);
        return result;
    }

    private MockHttpServletRequest request(String bearer) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/resource/123");
        request.setAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, "/resource/{id}");
        if (bearer != null) {
            request.addHeader("Authorization", bearer);
        }
        return request;
    }

    private MockHttpServletResponse response() {
        return new MockHttpServletResponse();
    }
}
