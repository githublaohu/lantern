package example.host.rbac;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatClient;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatConfig;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatPlatformAuthService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RbacEnterpriseWechatTest {

    static EnterpriseWechatConfig config() {
        EnterpriseWechatConfig config = new EnterpriseWechatConfig();
        config.setEnabled(true);
        config.setCorpId("ww-test-corp");
        config.setAgentId("1000002");
        config.setSecret("test-secret-only");
        config.setRedirectUri("https://dashboard.example/account/enterprise-wechat/callback");
        return config;
    }

    @Test
    void officialCorpAppUrlEncodesCallbackAndNeverIncludesSecret() {
        EnterpriseWechatConfig config = config();
        config.validate();
        String url = new EnterpriseWechatClient(config).authorizeUrl("test-state");
        assertThat(url).startsWith("https://login.work.weixin.qq.com/wwlogin/sso/login?login_type=CorpApp")
                .contains("&appid=ww-test-corp", "&agentid=1000002", "&state=test-state")
                .doesNotContain(config.getSecret(), "dashboard.example/account");
        assertThat(URLDecoder.decode(url, StandardCharsets.UTF_8)).contains("redirect_uri=" + config.getRedirectUri());
    }

    @Test
    void exchangesCodeOnServerAndCachesOnlyApplicationToken() {
        ScriptedClient client = new ScriptedClient(token("first"), json("{\"errcode\":0,\"userid\":\"Member-A\"}"),
                json("{\"errcode\":0,\"userid\":\"Member-B\"}"));
        assertThat(client.memberId("one+&code")).isEqualTo("Member-A");
        assertThat(client.memberId("two")).isEqualTo("Member-B");
        assertThat(client.addresses).hasSize(3);
        assertThat(client.addresses.get(0)).startsWith("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=");
        assertThat(client.addresses.get(1)).startsWith("https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?")
                .contains("access_token=first", "code=one%2B%26code");
    }

    @Test
    void expiredApplicationTokenIsRefreshedOnce() {
        ScriptedClient client = new ScriptedClient(token("old"), json("{\"errcode\":42001}"), token("new"),
                json("{\"errcode\":0,\"userid\":\"member\"}"));
        assertThat(client.memberId("single-use-code")).isEqualTo("member");
        assertThat(client.addresses).hasSize(4);
        assertThat(client.addresses.get(3)).contains("access_token=new");
    }

    @Test
    void externalVisitorsAndInvalidOrReusedCodesCannotBecomeLocalUsers() {
        for (String response : Arrays.asList("{\"errcode\":0,\"openid\":\"external\"}",
                "{\"errcode\":40029}", "{\"errcode\":40163}", "{\"errcode\":0,\"userid\":\" \"}")) {
            assertThat(new ScriptedClient(token("token"), json(response)).memberId("code")).isNull();
        }
        ScriptedClient noNetwork = new ScriptedClient();
        assertThat(noNetwork.memberId(null)).isNull();
        assertThat(noNetwork.memberId(" ")).isNull();
        assertThat(noNetwork.memberId(new String(new char[513]).replace('\0', 'a'))).isNull();
        assertThat(noNetwork.addresses).isEmpty();
    }

    @Test
    void malformedResponsesAndProviderFailuresDoNotAuthenticate() {
        for (String response : Arrays.asList("{}", "{\"errcode\":\"0\",\"userid\":\"member\"}",
                "{\"errcode\":50001,\"errmsg\":\"private provider detail\"}")) {
            assertThatThrownBy(() -> new ScriptedClient(token("token"), json(response)).memberId("code"))
                    .isInstanceOf(IllegalStateException.class).hasMessageNotContaining("private");
        }
        assertThatThrownBy(() -> new ScriptedClient(json("{\"errcode\":0,\"expires_in\":7200}")).memberId("code"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void providerIdentityOverridesAllCallerIdentityAndSensitiveFieldsAreRemoved() {
        EnterpriseWechatClient client = mock(EnterpriseWechatClient.class);
        when(client.memberId("code")).thenReturn("Verified-Member");
        AtomicInteger lookups = new AtomicInteger();
        UserInfo database = new UserInfo();
        database.setUiId(42L);
        database.setSystemId(915L);
        database.setUiName("existing-user");
        database.setUiSalt("private-salt");
        database.setUiSaltPassword("private-hash");
        database.setUiPassword("private-password");
        database.setUiToken("legacy-token");
        EnterpriseWechatPlatformAuthService service = new EnterpriseWechatPlatformAuthService(client, member -> {
            assertThat(member).isEqualTo("Verified-Member");
            lookups.incrementAndGet();
            return database;
        });
        UserInfo forged = new UserInfo();
        forged.setUiId(999L);
        forged.setSystemId(916L);
        forged.setUiToken("code");
        UserInfo result = service.auth(forged).getUserInfo();
        assertThat(result.getUiId()).isEqualTo(42L);
        assertThat(result.getSystemId()).isEqualTo(915L);
        assertThat(result.getUiToken()).isNull();
        assertThat(result.getUiSalt()).isNull();
        assertThat(result.getUiSaltPassword()).isNull();
        assertThat(result.getUiPassword()).isNull();
        assertThat(lookups).hasValue(1);
    }

    @Test
    void unboundMembersAreRejectedWithoutReturningUserData() {
        EnterpriseWechatClient client = mock(EnterpriseWechatClient.class);
        when(client.memberId("code")).thenReturn("unbound");
        UserInfo input = new UserInfo();
        input.setUiToken("code");
        EnterpriseWechatPlatformAuthService service = new EnterpriseWechatPlatformAuthService(client, member -> null);
        assertThat(service.auth(input).getErrorMessage()).isNotEmpty();
        assertThat(service.auth(input).getUserInfo()).isNull();
    }

    @Test
    void configurationRequiresCredentialsAndFixedSecureCallback() {
        for (String callback : Arrays.asList("http://dashboard.example/account/enterprise-wechat/callback",
                "https://dashboard.example/wrong", "https://user@dashboard.example/account/enterprise-wechat/callback",
                "https://dashboard.example/account/enterprise-wechat/callback?next=external")) {
            EnterpriseWechatConfig config = config();
            config.setRedirectUri(callback);
            assertThatThrownBy(config::validate).isInstanceOf(IllegalArgumentException.class);
        }
        EnterpriseWechatConfig local = config();
        local.setRedirectUri("http://localhost:8080/account/enterprise-wechat/callback");
        local.validate();
        local.setSecret("");
        assertThatThrownBy(local::validate).hasMessageContaining("secret is required");
    }

    private static JSONObject json(String value) {
        return JSONObject.parseObject(value);
    }

    private static JSONObject token(String value) {
        JSONObject result = new JSONObject();
        result.put("errcode", 0);
        result.put("access_token", value);
        result.put("expires_in", 7200);
        return result;
    }

    private static class ScriptedClient extends EnterpriseWechatClient {
        private final List<JSONObject> responses;
        private final List<String> addresses = new ArrayList<>();

        ScriptedClient(JSONObject... responses) {
            super(config());
            this.responses = new ArrayList<>(Arrays.asList(responses));
        }

        @Override
        protected JSONObject getJson(String address) {
            addresses.add(address);
            return responses.remove(0);
        }
    }
}
