package com.lamp.lantern.plugins.springmvc.support;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.config.AuthenticationFlowConfig;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.authentication.LanternAuthFlow;
import com.lamp.lantern.plugins.core.login.HandlerExecute;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Thin HTTP entry point for the existing Lantern login chain. */
@RestController
@ConditionalOnBean(name = "lanternDatabaseLogin")
public class LanternDatabaseLoginController {

    private final HandlerExecute login;
    private final Consumer<String> logout;
    private final LanternAuthFlow flow;

    public LanternDatabaseLoginController(HandlerExecute login, Consumer<String> logout, AuthenticationFlowConfig config) {
        this.login = login;
        this.logout = logout;
        this.flow = new LanternAuthFlow(config);
    }

    @PostMapping("/account/first/accountLogin")
    public ResultObject<?> accountLogin(@RequestBody UserInfo input, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
        ResultObject<?> result = login.execute(input);
        if (result.getCode() != 200) {
            response.setStatus(result.getCode() == 503 ? 503 : 401);
        }
        return result;
    }

    @PostMapping("/account/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
        String token = flow.getToken(request);
        if (token == null) {
            flow.failed(response);
            return;
        }
        try {
            logout.accept(token);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }
}
