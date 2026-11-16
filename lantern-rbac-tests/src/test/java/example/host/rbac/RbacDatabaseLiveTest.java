package example.host.rbac;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.authentication.service.CacheAuthenticationService;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.core.login.LanternContext;
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
import java.util.List;
import java.util.UUID;
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

    @BeforeAll
    void startHostWithExistingEventMeshYaml() {
        SpringApplication application = new SpringApplication(RbacHost.class);
        context = application.run("--spring.config.name=application-eventmesh-contract", "--server.port=0",
                "--spring.main.banner-mode=off", "--spring.application.name=lantern-mysql-contract-test",
                "--test.datasource.url=" + System.getProperty("test.mysql.url",
                        "jdbc:mysql://127.0.0.1:13317/lantern_rbac?useSSL=false&allowPublicKeyRetrieval=true&connectTimeout=3000&socketTimeout=3000"),
                "--test.datasource.username=" + System.getProperty("test.mysql.username", "lantern"),
                "--test.datasource.password=" + System.getProperty("test.mysql.password", "lantern"),
                "--lantern.authenticationConfig.authenticationFlowConfig.includePaths[0]=" + HostProbeController.CLUSTER_ROUTE,
                "--lantern.authenticationConfig.authenticationFlowConfig.includePaths[1]=/rbac/**");
        jdbc = new JdbcTemplate(context.getBean(DataSource.class));
        mvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).build();
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
