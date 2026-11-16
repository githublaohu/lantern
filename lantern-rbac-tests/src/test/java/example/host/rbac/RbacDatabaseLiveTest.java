package example.host.rbac;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.authentication.service.CacheAuthenticationService;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatClient;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatConfig;
import com.alibaba.fastjson.JSONObject;
import example.host.HostProbeController;
import example.host.RbacHost;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.Cookie;
import javax.sql.DataSource;
import org.apache.dubbo.config.spring.reference.ReferenceBeanManager;
import org.apache.dubbo.rpc.cluster.ClusterInvoker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Real MySQL, real Spring auto-configuration and real Dubbo injvm; no mocked authentication services. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RbacDatabaseLiveTest {

    private ConfigurableApplicationContext context;
    private JdbcTemplate jdbc;
    private MockMvc mvc;
    private long owner;
    private long outsider;
    private long role;
    private long cluster;
    private long order;
    private String token;
    private String outsiderToken;
    private final List<Long> extraUsers = new ArrayList<>();
    private final List<Long> extraResources = new ArrayList<>();
    private final List<String> oauthStates = new ArrayList<>();
    private WechatClient wechat;

    @BeforeAll
    void startHostWithExistingEventMeshYaml() {
        SpringApplication application = new SpringApplication(RbacHost.class, WechatTestConfiguration.class);
        context = application.run("--spring.config.name=application-eventmesh-contract", "--server.port=0",
                "--spring.main.banner-mode=off", "--spring.application.name=lantern-mysql-contract-test",
                "--test.datasource.url=" + System.getProperty("test.mysql.url",
                        "jdbc:mysql://127.0.0.1:13317/lantern_rbac?useSSL=false&allowPublicKeyRetrieval=true&connectTimeout=3000&socketTimeout=3000"),
                "--test.datasource.username=" + System.getProperty("test.mysql.username", "lantern"),
                "--test.datasource.password=" + System.getProperty("test.mysql.password", "lantern"),
                "--lantern.authenticationConfig.authenticationFlowConfig.includePaths[0]=" + HostProbeController.CLUSTER_ROUTE,
                "--lantern.authenticationConfig.authenticationFlowConfig.includePaths[1]=/rbac/**",
                "--lantern.authenticationConfig.authenticationFlowConfig.includePaths[2]=/account/**",
                "--lantern.loginConfig.enterpriseWechat.enabled=true",
                "--lantern.loginConfig.enterpriseWechat.corpId=ww-test-corp",
                "--lantern.loginConfig.enterpriseWechat.agentId=1000002",
                "--lantern.loginConfig.enterpriseWechat.secret=test-secret-only",
                "--lantern.loginConfig.enterpriseWechat.redirectUri=https://dashboard.example/account/enterprise-wechat/callback");
        jdbc = new JdbcTemplate(context.getBean(DataSource.class));
        mvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).build();
        wechat = context.getBean(WechatClient.class);
    }

    @BeforeEach
    void createRbacFixture() {
        token = "a" + UUID.randomUUID();
        outsiderToken = "b" + UUID.randomUUID();
        owner = user(915, token);
        outsider = user(915, outsiderToken);
        role = insert("INSERT INTO `role` (role_type_id,system_id,role_name,start_time,valid_time,create_user_id,update_user_id)"
                + " VALUES (0,915,'operator',DATE_SUB(NOW(),INTERVAL 1 DAY),DATE_ADD(NOW(),INTERVAL 1 DAY),0,0)");
        cluster = resource("POST", HostProbeController.CLUSTER_ROUTE, "ROLE");
        order = resource("GET", HostProbeController.ORDER_ROUTE, "ROLE");
        jdbc.update("INSERT INTO user_role_relation (urr_type,role_id,user_id,start_time,valid_time,create_user_id,update_user_id)"
                + " VALUES ('USER',?,?,DATE_SUB(NOW(),INTERVAL 1 DAY),DATE_ADD(NOW(),INTERVAL 1 DAY),0,0)", role, owner);
        grant(cluster);
        grant(order);
    }

    @AfterEach
    void removeOnlyThisTestsRecords() {
        jdbc.update("DELETE FROM platform_user_info WHERE user_id IN (?,?)", owner, outsider);
        for (Long id : extraUsers) {
            jdbc.update("DELETE FROM platform_user_info WHERE user_id=?", id);
        }
        for (String state : oauthStates) {
            jdbc.update("DELETE FROM lantern_oauth_state WHERE state_hash=?", DigestUtils.sha256Hex(state));
        }
        oauthStates.clear();
        wechat.codes.clear();
        jdbc.update("DELETE FROM user_login_record WHERE ui_id IN (?,?)", owner, outsider);
        jdbc.update("DELETE FROM resource_role_relation WHERE role_id=?", role);
        jdbc.update("DELETE FROM user_role_relation WHERE role_id=?", role);
        jdbc.update("DELETE FROM `role` WHERE role_id=?", role);
        jdbc.update("DELETE FROM resources WHERE resource_id IN (?,?)", cluster, order);
        for (Long id : extraResources) {
            jdbc.update("DELETE FROM resources WHERE resource_id=?", id);
        }
        jdbc.update("DELETE FROM user_info WHERE ui_id IN (?,?)", owner, outsider);
        for (Long id : extraUsers) {
            jdbc.update("DELETE FROM user_login_record WHERE ui_id=?", id);
            jdbc.update("DELETE FROM user_info WHERE ui_id=?", id);
        }
        extraUsers.clear();
        extraResources.clear();
        LanternContext.removeContext();
    }

    @AfterAll
    void stopHost() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void enterpriseWechatLoginUsesExistingTokenSessionRbacAndLogout() throws Exception {
        bindWechat(915, owner, "ww-test-corp", "1000002", "Member-A");
        String issued = wechatToken("Member-A", owner);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM user_login_record WHERE system_id=915 AND ui_id=?"
                + " AND ul_session_id=? AND ul_login_status='SUCCESS' AND ul_quit_time>NOW()", Integer.class,
                owner, issued)).isEqualTo(1);
        assertThat(context.getBeansOfType(RedisConnectionFactory.class)).isEmpty();
        cluster(issued).andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(owner));
        jdbc.update("UPDATE resource_role_relation SET is_delete=1 WHERE role_id=?", role);
        cluster(issued).andExpect(status().isForbidden());
        jdbc.update("UPDATE resource_role_relation SET is_delete=0 WHERE role_id=?", role);
        cluster(issued).andExpect(status().isOk());
        mvc.perform(post("/account/logout").header("Authorization", "Bearer " + issued))
                .andExpect(status().isNoContent());
        cluster(issued).andExpect(status().isUnauthorized());
    }

    @Test
    void enterpriseWechatDoesNotGrantRolesToAnOtherwiseValidMember() throws Exception {
        bindWechat(915, outsider, "ww-test-corp", "1000002", "Member-B");
        cluster(wechatToken("Member-B", outsider)).andExpect(status().isForbidden());
    }

    @Test
    void oauthStateIsBrowserBoundStoredAsDigestsAndConsumedOnlyOnce() throws Exception {
        bindWechat(915, owner, "ww-test-corp", "1000002", "Member-A");
        WechatRequest request = startWechat();
        Map<String, Object> row = jdbc.queryForMap("SELECT state_hash,browser_hash FROM lantern_oauth_state"
                + " WHERE state_hash=?", DigestUtils.sha256Hex(request.state));
        assertThat(row.get("state_hash")).isEqualTo(DigestUtils.sha256Hex(request.state));
        assertThat(row.get("browser_hash")).isEqualTo(DigestUtils.sha256Hex(request.browser.getValue()));
        int exchanges = wechat.identityRequests.get();
        callback(request, code("Member-A"), null).andExpect(status().isUnauthorized());
        callback(request, code("Member-A"), new Cookie(request.browser.getName(), "00000000000000000000000000000000"))
                .andExpect(status().isUnauthorized());
        assertThat(wechat.identityRequests).hasValue(exchanges);
        callback(request, code("Member-A"), request.browser).andExpect(status().isOk());
        callback(request, code("Member-A"), request.browser).andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("Authorization"));
        assertThat(wechat.identityRequests).hasValue(exchanges + 1);
    }

    @Test
    void expiredOrCrossSystemStateCannotReachProviderOrCreateSession() throws Exception {
        WechatRequest expired = startWechat();
        jdbc.update("UPDATE lantern_oauth_state SET expires_at=DATE_SUB(NOW(),INTERVAL 1 SECOND) WHERE state_hash=?",
                DigestUtils.sha256Hex(expired.state));
        callback(expired, code("Member-A"), expired.browser).andExpect(status().isUnauthorized());
        WechatRequest otherSystem = startWechat();
        jdbc.update("UPDATE lantern_oauth_state SET system_id=916 WHERE state_hash=?", DigestUtils.sha256Hex(otherSystem.state));
        callback(otherSystem, code("Member-A"), otherSystem.browser).andExpect(status().isUnauthorized());
        assertNoWechatSession();
    }

    @Test
    void concurrentCallbacksCanConsumeStateOnlyOnce() throws Exception {
        bindWechat(915, owner, "ww-test-corp", "1000002", "Member-A");
        WechatRequest request = startWechat();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            String first = code("Member-A");
            String second = code("Member-A");
            Future<Integer> one = executor.submit(() -> callback(request, first, request.browser)
                    .andReturn().getResponse().getStatus());
            Future<Integer> two = executor.submit(() -> callback(request, second, request.browser)
                    .andReturn().getResponse().getStatus());
            assertThat(Arrays.asList(one.get(), two.get())).containsExactlyInAnyOrder(200, 401);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM user_login_record WHERE ui_id=?", Integer.class, owner))
                    .isEqualTo(1);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void duplicateCookiesOrParametersAreRejectedBeforeIdentityExchange() throws Exception {
        WechatRequest request = startWechat();
        String code = code("Member-A");
        mvc.perform(get(EnterpriseWechatConfig.CALLBACK_PATH).param("state", request.state).param("code", code)
                .cookie(request.browser, request.browser)).andExpect(status().isUnauthorized());
        mvc.perform(get(EnterpriseWechatConfig.CALLBACK_PATH).param("state", request.state, request.state)
                .param("code", code).cookie(request.browser)).andExpect(status().isUnauthorized());
        mvc.perform(get(EnterpriseWechatConfig.CALLBACK_PATH).param("state", request.state).param("code", code, code)
                .cookie(request.browser)).andExpect(status().isUnauthorized());
        assertNoWechatSession();
    }

    @Test
    void memberBindingsAreIsolatedBySystemCorporationApplicationAndExactMemberId() throws Exception {
        bindWechat(916, owner, "ww-test-corp", "1000002", "Member-A");
        bindWechat(915, owner, "another-corp", "1000002", "Member-A");
        bindWechat(915, owner, "ww-test-corp", "1000003", "Member-A");
        bindWechat(915, owner, "ww-test-corp", "1000002", "member-a");
        WechatRequest request = startWechat();
        callback(request, code("Member-A"), request.browser).andExpect(status().isUnauthorized());
        assertNoWechatSession();
    }

    @Test
    void unboundAndDuplicateActiveBindingsCannotCreateSession() throws Exception {
        WechatRequest unbound = startWechat();
        callback(unbound, code("Member-A"), unbound.browser).andExpect(status().isUnauthorized());
        bindWechat(915, owner, "ww-test-corp", "1000002", "Member-A");
        bindWechat(915, outsider, "ww-test-corp", "1000002", "Member-A");
        WechatRequest duplicate = startWechat();
        callback(duplicate, code("Member-A"), duplicate.browser).andExpect(status().isUnauthorized());
        assertNoWechatSession();
    }

    @ParameterizedTest
    @ValueSource(strings = {"pui_status=1", "is_delete=1", "pui_type='THIRD'", "pui_auth_channel='Wechat'"})
    void disabledDeletedOrWrongTypeBindingsCannotLogin(String change) throws Exception {
        long binding = bindWechat(915, owner, "ww-test-corp", "1000002", "Member-A");
        jdbc.update("UPDATE platform_user_info SET " + change + " WHERE pui_id=?", binding);
        WechatRequest request = startWechat();
        callback(request, code("Member-A"), request.browser).andExpect(status().isUnauthorized());
        assertNoWechatSession();
    }

    @ParameterizedTest
    @ValueSource(strings = {"is_delete=1", "ui_allow_login='INACTIVE'"})
    void disabledOrDeletedLocalUserCannotLoginThroughWechat(String change) throws Exception {
        bindWechat(915, owner, "ww-test-corp", "1000002", "Member-A");
        jdbc.update("UPDATE user_info SET " + change + " WHERE ui_id=?", owner);
        WechatRequest request = startWechat();
        callback(request, code("Member-A"), request.browser).andExpect(status().isUnauthorized());
        assertNoWechatSession();
    }

    @Test
    void providerFailureAndInvalidCodeNeverIssueTokenOrLeakProviderDetails() throws Exception {
        bindWechat(915, owner, "ww-test-corp", "1000002", "Member-A");
        WechatRequest invalid = startWechat();
        callback(invalid, "unknown-code", invalid.browser).andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("Authorization"));
        WechatRequest failed = startWechat();
        String failing = UUID.randomUUID().toString();
        wechat.codes.put(failing, "PROVIDER_ERROR");
        String body = callback(failed, failing, failed.browser).andExpect(status().isServiceUnavailable())
                .andExpect(header().doesNotExist("Authorization")).andReturn().getResponse().getContentAsString();
        assertThat(body).doesNotContain("private-provider-detail", "test-secret-only");
        assertNoWechatSession();
    }

    private void assertNoWechatSession() {
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM user_login_record WHERE ui_id IN (?,?)", Integer.class,
                owner, outsider)).isZero();
    }

    private long bindWechat(long system, long user, String corp, String app, String member) {
        return insert("INSERT INTO platform_user_info (system_id,user_id,corporation_id,app_id,pui_open_id,pui_union_id,"
                + "pui_type,pui_auth_channel,create_user_id,update_user_id) VALUES (?,?,?,?,?,'','PLATFORM','EnterpriseWechat',0,0)",
                system, user, corp, app, member);
    }

    private String code(String member) {
        String code = UUID.randomUUID().toString();
        wechat.codes.put(code, member);
        return code;
    }

    private WechatRequest startWechat() throws Exception {
        org.springframework.mock.web.MockHttpServletResponse response = mvc.perform(get(EnterpriseWechatConfig.AUTHORIZE_PATH))
                .andExpect(status().isFound()).andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(header().string("Referrer-Policy", "no-referrer")).andReturn().getResponse();
        String location = response.getHeader("Location");
        assertThat(location).startsWith("https://login.work.weixin.qq.com/wwlogin/sso/login?login_type=CorpApp")
                .doesNotContain("test-secret-only", "uiToken");
        String state = org.springframework.web.util.UriComponentsBuilder.fromUriString(location).build()
                .getQueryParams().getFirst("state");
        String cookie = response.getHeader("Set-Cookie");
        assertThat(cookie).contains("HttpOnly", "SameSite=Lax", "Secure", "Max-Age=300",
                "Path=/account/enterprise-wechat/callback");
        oauthStates.add(state);
        return new WechatRequest(state, new Cookie("lantern_wecom_state", cookie.split(";", 2)[0].split("=", 2)[1]));
    }

    private ResultActions callback(WechatRequest request, String code, Cookie browser) throws Exception {
        org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder callback =
                get(EnterpriseWechatConfig.CALLBACK_PATH).param("code", code).param("state", request.state);
        if (browser != null) {
            callback.cookie(browser);
        }
        return mvc.perform(callback);
    }

    private String wechatToken(String member, long userId) throws Exception {
        WechatRequest request = startWechat();
        org.springframework.mock.web.MockHttpServletResponse response = callback(request, code(member), request.browser)
                .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.uiId").value(userId)).andExpect(jsonPath("$.data.systemId").value(915))
                .andExpect(jsonPath("$.data.uiSalt").doesNotExist()).andExpect(jsonPath("$.data.uiPassword").doesNotExist())
                .andExpect(jsonPath("$.data.uiSaltPassword").doesNotExist()).andReturn().getResponse();
        String issued = new ObjectMapper().readTree(response.getContentAsString()).path("data").path("uiToken").asText();
        assertThat(issued).matches("[a-f0-9]{32}");
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + issued);
        assertThat(response.getHeader("Set-Cookie")).contains("Max-Age=0");
        return issued;
    }

    private static class WechatRequest {
        final String state;
        final Cookie browser;

        WechatRequest(String state, Cookie browser) {
            this.state = state;
            this.browser = browser;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class WechatTestConfiguration {
        @Bean
        WechatClient mockWechatApi(EnterpriseWechatConfig config) {
            return new WechatClient(config);
        }
    }

    /** Only the external HTTPS responses are simulated; binding, state, token, SQL and Dubbo are real. */
    static class WechatClient extends EnterpriseWechatClient {
        final Map<String, String> codes = new ConcurrentHashMap<>();
        final AtomicInteger identityRequests = new AtomicInteger();

        WechatClient(EnterpriseWechatConfig config) {
            super(config);
        }

        @Override
        protected JSONObject getJson(String address) {
            JSONObject response = new JSONObject();
            response.put("errcode", 0);
            if (address.contains("/gettoken?")) {
                response.put("access_token", "test-application-token");
                response.put("expires_in", 7200);
            } else {
                identityRequests.incrementAndGet();
                String code = org.springframework.web.util.UriComponentsBuilder.fromUriString(address).build()
                        .getQueryParams().getFirst("code");
                String member = codes.remove(code);
                if ("PROVIDER_ERROR".equals(member)) {
                    throw new IllegalStateException("private-provider-detail");
                }
                if (member == null) {
                    response.put("errcode", 40029);
                } else {
                    response.put("userid", member);
                }
            }
            return response;
        }
    }

    @Test
    void createsSchemaAndUsesLocalDubboWithoutRedisOrCacheTasks() throws Exception {
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=DATABASE()"
                + " AND table_name IN ('platform_user_info','resource_role_relation','resources','role','role_type',"
                + "'user_info','user_login_record','user_role_relation')", Integer.class)).isEqualTo(8);
        assertThat(context.getBeansOfType(DataSource.class)).hasSize(1);
        assertThat(context.getBeansOfType(RedisConnectionFactory.class)).isEmpty();
        assertThat(context.getBeansOfType(HandlerService.class)).isEmpty();
        assertThat(context.getBeansOfType(CacheAuthenticationService.class)).isEmpty();
        assertThat(ReflectionTestUtils.getField(context.getBean(AuthenticationManager.class),
                "scheduledThreadPoolExecutor")).isNull();
        ReferenceBeanManager references = context.getBean(ReferenceBeanManager.class);
        assertThat(references.getReferences()).extracting(ref -> ref.getInterfaceClass())
                .containsExactlyInAnyOrder(AuthenticationService.class, AuthenticationPolicyService.class);
        references.getReferences().forEach(ref -> {
            ClusterInvoker<?> invoker = (ClusterInvoker<?>) ref.getReferenceConfig().getInvoker();
            assertThat(invoker.getDirectory().getAllInvokers()).isNotEmpty()
                    .allSatisfy(local -> assertThat(local.getUrl().getProtocol()).isEqualTo("injvm"));
        });
        cluster(token).andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(owner))
                .andExpect(jsonPath("$.message").value("business-data"));
        assertThat(LanternContext.getContext().getUserInfo()).isNull();
        mvc.perform(post("/public/echo").contentType(MediaType.APPLICATION_JSON).content("{\"message\":\"ok\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.message").value("ok"));
        mvc.perform(post("/public/annotated").contentType(MediaType.APPLICATION_JSON).content("{\"message\":\"ok\",\"userId\":999}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.userId").doesNotExist());
    }

    @Test
    void distinguishesIdentityAndPermissionFailuresAndTokenCase() throws Exception {
        cluster(null).andExpect(status().isUnauthorized()).andExpect(header().string("WWW-Authenticate", "Bearer"));
        cluster("invalid").andExpect(status().isUnauthorized());
        cluster(token.toUpperCase()).andExpect(status().isUnauthorized());
        cluster(outsiderToken).andExpect(status().isForbidden());
        cluster(token).andExpect(status().isOk());
        mvc.perform(post(HostProbeController.CLUSTER_ROUTE).header("Authorization", "Bearer " + token, "Bearer " + outsiderToken)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void onlyUsesActiveUniqueUsersFromConfiguredSystem() throws Exception {
        extraUsers.add(user(916, outsiderToken));
        jdbc.update("UPDATE user_info SET system_id=916 WHERE ui_id=?", owner);
        cluster(token).andExpect(status().isUnauthorized());
        jdbc.update("UPDATE user_info SET system_id=915,ui_allow_login='INACTIVE' WHERE ui_id=?", owner);
        cluster(token).andExpect(status().isUnauthorized());
        jdbc.update("UPDATE user_info SET ui_allow_login='ACTIVE',is_delete=1 WHERE ui_id=?", owner);
        cluster(token).andExpect(status().isUnauthorized());
        jdbc.update("UPDATE user_info SET is_delete=0 WHERE ui_id=?", owner);
        cluster(token).andExpect(status().isOk());
        extraUsers.add(user(915, token));
        cluster(token).andExpect(status().isUnauthorized());
    }

    @Test
    void revocationAndTokenChangesTakeEffectOnNextRequest() throws Exception {
        cluster(token).andExpect(status().isOk());
        jdbc.update("DELETE FROM resource_role_relation WHERE role_id=? AND resource_id=?", role, cluster);
        cluster(token).andExpect(status().isForbidden());
        grant(cluster);
        cluster(token).andExpect(status().isOk());
        jdbc.update("UPDATE user_info SET ui_token=? WHERE ui_id=?", token + "-rotated", owner);
        cluster(token).andExpect(status().isUnauthorized());
        cluster(token + "-rotated").andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "UPDATE `role` SET is_delete=1 WHERE role_id=?",
            "UPDATE `role` SET valid_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE `role` SET end_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE `role` SET start_time=DATE_ADD(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE `role` SET system_id=916 WHERE role_id=?",
            "UPDATE user_role_relation SET is_delete=1 WHERE role_id=?",
            "UPDATE user_role_relation SET urr_type='OTHER' WHERE role_id=?",
            "UPDATE user_role_relation SET valid_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE user_role_relation SET end_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE user_role_relation SET start_time=DATE_ADD(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE resource_role_relation SET is_delete=1 WHERE role_id=?",
            "UPDATE resource_role_relation SET valid_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE resource_role_relation SET end_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE role_id=?",
            "UPDATE resource_role_relation SET start_time=DATE_ADD(NOW(),INTERVAL 1 DAY) WHERE role_id=?"})
    void rejectsInvalidRolesAndRelations(String mutation) throws Exception {
        cluster(token).andExpect(status().isOk());
        jdbc.update(mutation, role);
        cluster(token).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "UPDATE resources SET is_delete=1 WHERE resource_id=?",
            "UPDATE resources SET valid_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE resource_id=?",
            "UPDATE resources SET end_time=DATE_SUB(NOW(),INTERVAL 1 DAY) WHERE resource_id=?",
            "UPDATE resources SET start_time=DATE_ADD(NOW(),INTERVAL 1 DAY) WHERE resource_id=?",
            "UPDATE resources SET system_id=916 WHERE resource_id=?",
            "UPDATE resources SET resource_operate='GET' WHERE resource_id=?",
            "UPDATE resources SET resource_type='OTHER' WHERE resource_id=?",
            "UPDATE resources SET resource_name=UPPER(resource_name) WHERE resource_id=?"})
    void missingOrInvalidHttpPolicyCannotBypassProtection(String mutation) throws Exception {
        jdbc.update(mutation, cluster);
        cluster(token).andExpect(status().isForbidden());
    }

    @Test
    void routeTemplateAndDatabaseModesAreAppliedInRealTime() throws Exception {
        mvc.perform(get("/rbac/orders/123").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
        mvc.perform(get("/rbac/orders/123")).andExpect(status().isUnauthorized());
        jdbc.update("UPDATE resources SET resource_conditions='USER' WHERE resource_id=?", order);
        mvc.perform(get("/rbac/orders/456").header("Authorization", "Bearer " + outsiderToken)).andExpect(status().isOk());
        mvc.perform(get("/rbac/orders/456")).andExpect(status().isUnauthorized());
        jdbc.update("UPDATE resources SET resource_conditions='PUBLIC' WHERE resource_id=?", order);
        mvc.perform(get("/rbac/orders/456")).andExpect(status().isOk());
        jdbc.update("UPDATE resources SET resource_conditions='ROLE' WHERE resource_id=?", order);
        mvc.perform(get("/rbac/orders/456").header("Authorization", "Bearer " + outsiderToken)).andExpect(status().isForbidden());
        mvc.perform(get("/rbac/orders/456").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }

    @Test
    void invalidOrDuplicatePoliciesFailClosed() throws Exception {
        jdbc.update("UPDATE resources SET resource_conditions='UNKNOWN' WHERE resource_id=?", cluster);
        cluster(token).andExpect(status().isServiceUnavailable());
        jdbc.update("UPDATE resources SET resource_conditions='ROLE' WHERE resource_id=?", cluster);
        extraResources.add(resource("POST", HostProbeController.CLUSTER_ROUTE, "PUBLIC"));
        cluster(token).andExpect(status().isServiceUnavailable());
    }

    @Test
    void directDubboCallsCannotForgeAnAuthorizedUserId() {
        AuthenticationService service = (AuthenticationService) context.getBean(ReferenceBeanManager.class).getReferences()
                .stream().filter(ref -> ref.getInterfaceClass() == AuthenticationService.class).findFirst().orElseThrow().getObject();
        AuthenticationData data = new AuthenticationData();
        data.setSystemId(915L);
        data.setToken(outsiderToken);
        data.setUserId(owner);
        data.setResourceId(cluster);
        assertThat(service.authentication(data).isSuccess()).isFalse();
        data.setToken(token);
        assertThat(service.authentication(data).isSuccess()).isTrue();
        assertThat(service.getUserInfo(data).getUserInfo().getUiSalt()).isNull();
        assertThat(service.getUserInfo(data).getUserInfo().getUiSaltPassword()).isNull();
        jdbc.update("UPDATE user_info SET system_id=916 WHERE ui_id=?", owner);
        data.setSystemId(null);
        assertThat(service.authentication(data).isSuccess()).isFalse();
    }

    @Test
    void grantsStartingNowAreEffectiveImmediately() throws Exception {
        jdbc.update("UPDATE `role` SET start_time=NOW() WHERE role_id=?", role);
        jdbc.update("UPDATE user_role_relation SET start_time=NOW() WHERE role_id=?", role);
        jdbc.update("UPDATE resource_role_relation SET start_time=NOW() WHERE role_id=?", role);
        jdbc.update("UPDATE resources SET start_time=NOW() WHERE resource_id=?", cluster);
        cluster(token).andExpect(status().isOk());
    }

    @Test
    void schemaInitializationIsIdempotentAndPreservesBusinessData() throws Exception {
        context.getBean(DataSourceScriptDatabaseInitializer.class).initializeDatabase();
        assertThat(jdbc.queryForObject("SELECT ui_token FROM user_info WHERE ui_id=?", String.class, owner)).isEqualTo(token);
        cluster(token).andExpect(status().isOk());
    }

    @Test
    void loginIssuesDatabaseSessionThenAuthorizesAndLogoutRevokesIt() throws Exception {
        String name = credentials(owner);
        String issued = loginToken(name);
        assertThat(issued).matches("[a-f0-9]{32}");
        assertThat(jdbc.queryForObject("SELECT ui_token FROM user_info WHERE ui_id=?", String.class, owner)).isEmpty();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM user_login_record WHERE system_id=915 AND ui_id=?"
                + " AND BINARY ul_session_id=? AND ul_login_status='SUCCESS' AND ul_quit_time>NOW()", Integer.class,
                owner, issued)).isEqualTo(1);
        cluster(issued).andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(owner));
        cluster(issued.toUpperCase()).andExpect(status().isUnauthorized());
        mvc.perform(post("/account/logout").header("Authorization", "Bearer " + issued))
                .andExpect(status().isNoContent());
        cluster(issued).andExpect(status().isUnauthorized());
        assertThat(LanternContext.getContext().getUserInfo()).isNull();
        assertThat(LanternContext.getContext().getToken()).isNull();
    }

    @Test
    void failedLoginNeverCreatesSessionOrReturnsToken() throws Exception {
        String name = credentials(owner);
        for (String[] invalid : new String[][]{{name, "wrong-password"}, {name, ""}, {"no-such-user", "correct-password"}}) {
            login(invalid[0], invalid[1]).andExpect(status().isUnauthorized())
                    .andExpect(header().doesNotExist("Authorization")).andExpect(jsonPath("$.data").doesNotExist());
        }
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM user_login_record WHERE ui_id=?", Integer.class, owner)).isZero();
        loginToken(name);
    }

    @Test
    void loginCannotUseDisabledDeletedForeignOrAmbiguousUsers() throws Exception {
        String name = credentials(owner);
        long foreign = user(916, "foreign-token");
        extraUsers.add(foreign);
        jdbc.update("UPDATE user_info SET ui_name=? WHERE ui_id=?", name, foreign);
        credentials(foreign);
        loginToken(name); // Same name in another system is ignored.
        jdbc.update("UPDATE user_info SET ui_allow_login='INACTIVE' WHERE ui_id=?", owner);
        login(name, "correct-password").andExpect(status().isUnauthorized());
        jdbc.update("UPDATE user_info SET ui_allow_login='ACTIVE',is_delete=1 WHERE ui_id=?", owner);
        login(name, "correct-password").andExpect(status().isUnauthorized());
        jdbc.update("UPDATE user_info SET is_delete=0,system_id=916 WHERE ui_id=?", owner);
        login(name, "correct-password").andExpect(status().isUnauthorized());
        jdbc.update("UPDATE user_info SET system_id=915 WHERE ui_id=?", owner);
        long duplicate = user(915, "duplicate-token");
        extraUsers.add(duplicate);
        jdbc.update("UPDATE user_info SET ui_name=? WHERE ui_id=?", name, duplicate);
        credentials(duplicate);
        login(name, "correct-password").andExpect(status().isUnauthorized());
    }

    @Test
    void sessionsExpireAndLogoutOnlyRevokesThePresentedSession() throws Exception {
        String name = credentials(owner);
        String first = loginToken(name);
        String second = loginToken(name);
        assertThat(second).isNotEqualTo(first);
        cluster(first).andExpect(status().isOk());
        cluster(second).andExpect(status().isOk());
        mvc.perform(post("/account/logout").header("Authorization", "Bearer " + first)).andExpect(status().isNoContent());
        cluster(first).andExpect(status().isUnauthorized());
        cluster(second).andExpect(status().isOk());
        jdbc.update("UPDATE user_login_record SET ul_quit_time=DATE_SUB(NOW(),INTERVAL 1 SECOND) WHERE ul_session_id=?", second);
        cluster(second).andExpect(status().isUnauthorized());
    }

    @Test
    void databaseSessionsStillRequireCurrentUserAndRolePermissions() throws Exception {
        String issued = loginToken(credentials(owner));
        String unprivileged = loginToken(credentials(outsider));
        cluster(unprivileged).andExpect(status().isForbidden());
        cluster(issued).andExpect(status().isOk());
        jdbc.update("DELETE FROM resource_role_relation WHERE role_id=? AND resource_id=?", role, cluster);
        cluster(issued).andExpect(status().isForbidden());
        grant(cluster);
        cluster(issued).andExpect(status().isOk());
        jdbc.update("UPDATE user_info SET ui_allow_login='INACTIVE' WHERE ui_id=?", owner);
        cluster(issued).andExpect(status().isUnauthorized());
    }

    @Test
    void logoutRequiresUnambiguousBearerAndCanRevokeLegacyTokens() throws Exception {
        mvc.perform(post("/account/logout")).andExpect(status().isUnauthorized());
        mvc.perform(post("/account/logout").header("Authorization", "Bearer " + token, "Bearer " + outsiderToken))
                .andExpect(status().isUnauthorized());
        cluster(token).andExpect(status().isOk());
        mvc.perform(post("/account/logout").header("Authorization", "Bearer " + token)).andExpect(status().isNoContent());
        cluster(token).andExpect(status().isUnauthorized());
        mvc.perform(post("/account/logout").header("Authorization", "Bearer " + token)).andExpect(status().isNoContent());
    }

    @Test
    void simultaneousLoginsKeepSessionsAndRequestContextsSeparate() throws Exception {
        String name = credentials(owner);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            List<Future<String>> requests = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                requests.add(executor.submit(() -> loginToken(name)));
            }
            HashSet<String> issued = new HashSet<>();
            for (Future<String> request : requests) {
                String bearer = request.get();
                issued.add(bearer);
                cluster(bearer).andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(owner));
            }
            assertThat(issued).hasSize(8);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM user_login_record WHERE ui_id=?", Integer.class, owner)).isEqualTo(8);
        } finally {
            executor.shutdownNow();
        }
    }

    private String credentials(long userId) {
        jdbc.update("UPDATE user_info SET ui_salt='test-salt',ui_salt_password=?,ui_token='' WHERE ui_id=?",
                DigestUtils.md5Hex("test-saltcorrect-password"), userId);
        return jdbc.queryForObject("SELECT ui_name FROM user_info WHERE ui_id=?", String.class, userId);
    }

    private ResultActions login(String name, String password) throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("uiName", name);
        input.put("uiPassword", password);
        input.put("uiId", -999L); // Never trust the caller's identity or system.
        input.put("systemId", 916);
        input.put("uiToken", "forged-client-token");
        return mvc.perform(post("/account/first/accountLogin").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)));
    }

    private String loginToken(String name) throws Exception {
        String body = login(name, "correct-password").andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.code").value(200)).andExpect(jsonPath("$.data.systemId").value(915))
                .andExpect(jsonPath("$.data.uiPassword").doesNotExist()).andExpect(jsonPath("$.data.uiSalt").doesNotExist())
                .andExpect(jsonPath("$.data.uiSaltPassword").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        String issued = new ObjectMapper().readTree(body).path("data").path("uiToken").asText();
        assertThat(issued).isNotEmpty().isNotEqualTo("forged-client-token");
        assertThat(LanternContext.getContext().getToken()).isNull();
        return issued;
    }

    private ResultActions cluster(String bearer) throws Exception {
        org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request = post(HostProbeController.CLUSTER_ROUTE)
                .contentType(MediaType.APPLICATION_JSON).content("{\"message\":\"business-data\",\"userId\":999}");
        if (bearer != null) {
            request.header("Authorization", "Bearer " + bearer);
        }
        return mvc.perform(request);
    }

    private long user(long system, String bearer) {
        String name = UUID.randomUUID().toString();
        return insert("INSERT INTO user_info (system_id,ui_name,ui_nickname,ui_id_card,ui_token,ui_salt,ui_salt_password,create_user_id,update_user_id)"
                + " VALUES (?,?, 'test-user',?,?, 'sensitive-salt','sensitive-password',0,0)", system, name, name, bearer);
    }

    private long resource(String method, String route, String mode) {
        return insert("INSERT INTO resources (system_id,resource_type,resource_name,resource_operate,resource_conditions,"
                + "start_time,valid_time,create_user_id,update_user_id) VALUES (915,'HTTP',?,?,?,"
                + "DATE_SUB(NOW(),INTERVAL 1 DAY),DATE_ADD(NOW(),INTERVAL 1 DAY),0,0)", route, method, mode);
    }

    private void grant(long resource) {
        jdbc.update("INSERT INTO resource_role_relation (resource_id,role_id,start_time,valid_time,create_user_id,update_user_id)"
                + " VALUES (?,?,DATE_SUB(NOW(),INTERVAL 1 DAY),DATE_ADD(NOW(),INTERVAL 1 DAY),0,0)", resource, role);
    }

    private long insert(String sql, Object... values) {
        GeneratedKeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            return statement;
        }, keys);
        return keys.getKey().longValue();
    }
}
