package com.lamp.lantern.plugins.springmvc.support;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatConfig;
import com.lamp.lantern.plugins.core.login.EnterpriseWechatLogin;
import java.net.URI;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Browser entry points; application secrets and Lantern tokens are never put into redirect URLs. */
@RestController
@ConditionalOnBean(EnterpriseWechatLogin.class)
public class LanternEnterpriseWechatController {

    private static final String COOKIE = "lantern_wecom_state";
    private final EnterpriseWechatLogin login;
    private final EnterpriseWechatConfig config;

    public LanternEnterpriseWechatController(EnterpriseWechatLogin login, EnterpriseWechatConfig config) {
        this.login = login;
        this.config = config;
    }

    @GetMapping(EnterpriseWechatConfig.AUTHORIZE_PATH)
    public void authorize(HttpServletResponse response) {
        secureResponse(response);
        try {
            EnterpriseWechatLogin.Authorization authorization = login.start();
            cookie(response, authorization.getBrowser(), config.getStateTtlSeconds());
            response.setHeader(HttpHeaders.LOCATION, authorization.getUrl());
            response.setStatus(HttpServletResponse.SC_FOUND);
        } catch (RuntimeException exception) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping(EnterpriseWechatConfig.CALLBACK_PATH)
    public ResultObject<?> callback(HttpServletRequest request, HttpServletResponse response) {
        secureResponse(response);
        cookie(response, "", 0);
        try {
            ResultObject<?> result = login.callback(single(request, "code"), single(request, "state"), browser(request));
            if (result.getCode() != 200) {
                response.setStatus(result.getCode() == 503 ? 503 : 401);
            }
            return result;
        } catch (RuntimeException exception) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResultObject.getResultObjectMessgae(503, "登录服务暂时不可用");
        }
    }

    private void cookie(HttpServletResponse response, String value, int maxAge) {
        URI callback = URI.create(config.getRedirectUri());
        response.addHeader(HttpHeaders.SET_COOKIE, COOKIE + "=" + value + "; Path=" + callback.getPath()
                + "; Max-Age=" + maxAge + "; HttpOnly; SameSite=Lax"
                + ("https".equals(callback.getScheme()) ? "; Secure" : ""));
    }

    private static void secureResponse(HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
        response.setHeader("Referrer-Policy", "no-referrer");
    }

    private static String single(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);
        return values != null && values.length == 1 ? values[0] : null;
    }

    private static String browser(HttpServletRequest request) {
        String value = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE.equals(cookie.getName())) {
                    if (value != null) {
                        return null;
                    }
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }
}
