package com.lamp.lantern.service.core.provider.configuration;

import com.lamp.lantern.plugins.api.config.AuthenticationFlowConfig;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.HandlerExecute;
import com.lamp.lantern.plugins.core.login.LanternAuthOperate;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.plugins.core.login.token.CreateTokenAuthHandler;
import com.lamp.lantern.plugins.core.login.token.TokenAndSessionConfig;
import com.lamp.lantern.plugins.core.servlet.SpringMVCServlet;
import com.lamp.lantern.service.core.provider.mapper.LoginRecordMapper;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/** Reuses the account auth, login execution chain and token handler; replaces only session persistence. */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = "lantern.loginConfig.enable", havingValue = "true")
public class LanternDatabaseLoginConfiguration {

    @Bean("lanternDatabaseLogin")
    public HandlerExecute databaseLogin(UserInfoMapper users, LoginRecordMapper sessions, Environment environment) {
        Binder binder = Binder.get(environment);
        AuthenticationFlowConfig flow = binder.bind("lantern.authentication-config.authentication-flow-config",
                AuthenticationFlowConfig.class).get();
        Assert.hasText(flow.getSystemId(), "Lantern systemId is required");
        Long systemId = Long.valueOf(flow.getSystemId());
        Assert.isTrue(systemId > 0, "Lantern systemId must be positive");
        Assert.isTrue("database".equals(environment.getProperty("lantern.loginConfig.sessionStore")),
                "Lantern local database login requires sessionStore=database");
        TokenAndSessionConfig tokenConfig = binder.bind("lantern.login-config.token", TokenAndSessionConfig.class).get();
        Assert.isTrue(tokenConfig.getTokenExpire() > 0, "Lantern tokenExpire must be positive");
        // The login response and request authentication must use the same token location/name.
        Assert.isTrue("header".equals(flow.getTokenSpot()), "Lantern database login requires header tokens");
        tokenConfig.setDataPosition(flow.getTokenSpot());
        tokenConfig.setTokenName(flow.getTokenName());
        CreateTokenAuthHandler tokenHandler = new CreateTokenAuthHandler() {
            @Override
            protected void saveToken(UserInfo user, String token) {
                Assert.isTrue(token.length() <= 36, "Lantern session token exceeds ul_session_id capacity");
                Assert.state(sessions.saveSession(systemId, user.getUiId(), token, tokenConfig.getTokenExpire()) == 1,
                        "Lantern login user is no longer active");
            }
        };
        tokenHandler.setConfig(tokenConfig);
        tokenHandler.init();
        LanternContext.removeContext();
        LanternAuthOperate account = new LanternAuthOperate(input -> {
            if (!StringUtils.hasText(input.getUiName())) {
                return null;
            }
            List<UserInfo> matches = users.findLoginUsers(systemId, input.getUiName());
            return matches.size() == 1 ? matches.get(0) : null;
        });
        HandlerExecute login = new HandlerExecute();
        LoginConfig config = new LoginConfig();
        config.setSystemName(flow.getSystemId());
        login.setLoginConfig(config);
        login.setServlet(new SpringMVCServlet());
        login.setAuthServiceMap(Collections.singletonMap(LoginType.FIRST, Collections.singletonMap("Lantern", account)));
        login.setHandlerList(Collections.singletonList(tokenHandler));
        return login;
    }

    @Bean("lanternDatabaseLogout")
    public Consumer<String> databaseLogout(UserInfoMapper users, LoginRecordMapper sessions,
            PlatformTransactionManager transactionManager, Environment environment) {
        Long systemId = Long.valueOf(Binder.get(environment).bind("lantern.authentication-config.authentication-flow-config",
                AuthenticationFlowConfig.class).get().getSystemId());
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        return token -> transaction.execute(status -> {
            sessions.revokeSession(systemId, token);
            users.revokeLegacyToken(systemId, token);
            return null;
        });
    }
}
