package example.host.rbac;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AuthHandler;
import com.lamp.lantern.plugins.core.login.HandlerExecute;
import com.lamp.lantern.plugins.core.login.LanternAuthOperate;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.login.token.CreateTokenAuthHandler;
import com.lamp.lantern.plugins.core.login.token.TokenAndSessionConfig;
import com.lamp.lantern.plugins.core.servlet.LanternServlet;
import com.lamp.lantern.plugins.core.token.TokenCreateMode;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RbacLoginCoreTest {

    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @AfterEach
    void clearContext() {
        LanternContext.removeContext();
    }

    @Test
    void wrongPasswordCannotReachTokenIssuanceButCorrectPasswordUsesDatabaseIdentity() {
        AtomicInteger saved = new AtomicInteger();
        HandlerExecute login = login(new CreateTokenAuthHandler() {
            @Override
            protected void saveToken(UserInfo user, String token) {
                assertThat(user.getUiId()).isEqualTo(7L);
                saved.incrementAndGet();
            }
        });
        assertThat(login.execute(input("wrong")).getCode()).isEqualTo(3000);
        assertThat(saved).hasValue(0);
        assertThat(response.getHeader("Authorization")).isNull();
        ResultObject<?> result = login.execute(input("correct"));
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(saved).hasValue(1);
        UserInfo user = (UserInfo) result.getData();
        assertThat(user.getUiId()).isEqualTo(7L);
        assertThat(user.getUiPassword()).isNull();
        assertThat(user.getUiSalt()).isNull();
        assertThat(user.getUiSaltPassword()).isNull();
        assertThat(user.getUiToken()).matches("[a-f0-9]{32}");
        assertThat(response.getHeader("Authorization")).isEqualTo("Bearer " + user.getUiToken());
        assertThat(LanternContext.getContext().getToken()).isNull();
        assertThat(LanternContext.getContext().getUserInfo()).isNull();
    }

    @Test
    void persistenceFailureDoesNotExposeOrReturnAnUnusableToken() {
        HandlerExecute login = login(new CreateTokenAuthHandler() {
            @Override
            protected void saveToken(UserInfo user, String token) {
                throw new IllegalStateException("private database details");
            }
        });
        ResultObject<?> result = login.execute(input("correct"));
        assertThat(result.getCode()).isEqualTo(503);
        assertThat(result.getData()).isNull();
        assertThat(result.getMessage()).doesNotContain("private database details");
        assertThat(response.getHeader("Authorization")).isNull();
        assertThat(LanternContext.getContext().getToken()).isNull();
    }

    @Test
    void rejectedPreconditionCannotCreateSession() {
        CreateTokenAuthHandler handler = mock(CreateTokenAuthHandler.class);
        HandlerExecute login = login(handler);
        AuthHandler reject = mock(AuthHandler.class);
        when(reject.authBefore(any())).thenReturn(ResultObject.getResultObjectMessgae(400, "rejected"));
        login.setHandlerList(Arrays.asList(reject, handler));
        assertThat(login.execute(input("correct")).getCode()).isEqualTo(400);
        verify(handler, never()).authAfter(any());
    }

    @Test
    void rejectedPostconditionIsNotOverwrittenWithSuccess() {
        CreateTokenAuthHandler handler = mock(CreateTokenAuthHandler.class);
        HandlerExecute login = login(handler);
        when(handler.authAfter(any())).thenReturn(ResultObject.getResultObjectMessgae(400, "rejected"));
        assertThat(login.execute(input("correct")).getCode()).isEqualTo(400);
    }

    @Test
    @SuppressWarnings("unchecked")
    void originalRedisStorageStillRunsThroughTheSameTokenHandler() {
        StatefulRedisConnection<String, String> connection = mock(StatefulRedisConnection.class);
        RedisCommands<String, String> commands = mock(RedisCommands.class);
        when(connection.sync()).thenReturn(commands);
        CreateTokenAuthHandler handler = new CreateTokenAuthHandler();
        handler.setConnection(connection);
        HandlerExecute login = login(handler);
        ResultObject<?> result = login.execute(input("correct"));
        assertThat(result.getCode()).isEqualTo(200);
        verify(commands).set(contains(((UserInfo) result.getData()).getUiToken()), contains("\"UserID\":\"7\""), any(SetArgs.class));
    }

    private HandlerExecute login(CreateTokenAuthHandler handler) {
        TokenAndSessionConfig config = new TokenAndSessionConfig();
        config.setTokenCreateMode(TokenCreateMode.UUID);
        config.setTokenName("Authorization");
        config.setDataPosition("header");
        handler.setConfig(config);
        handler.init();
        LanternContext.removeContext();
        LanternAuthOperate account = new LanternAuthOperate(input -> {
            UserInfo user = new UserInfo();
            user.setUiId(7L);
            user.setUiName("alice");
            user.setSystemId(915L);
            user.setUiSalt("salt-");
            user.setUiSaltPassword(DigestUtils.md5Hex("salt-correct"));
            return user;
        });
        HandlerExecute login = new HandlerExecute();
        login.setLoginConfig(new LoginConfig());
        LanternServlet servlet = mock(LanternServlet.class);
        when(servlet.getRequest()).thenReturn(new MockHttpServletRequest());
        when(servlet.getResponse()).thenReturn(response);
        login.setServlet(servlet);
        login.setHandlerList(Collections.singletonList(handler));
        login.setAuthServiceMap(Collections.singletonMap(LoginType.FIRST, Collections.singletonMap("Lantern", account)));
        return login;
    }

    private UserInfo input(String password) {
        UserInfo input = new UserInfo();
        input.setUiName("alice");
        input.setUiId(999L);
        input.setUiPassword(password);
        return input;
    }
}
