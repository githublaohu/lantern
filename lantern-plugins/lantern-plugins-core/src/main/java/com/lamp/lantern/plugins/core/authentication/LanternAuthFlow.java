package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.config.AuthenticationFlowConfig;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

/**
 * @author laohu
 */
public class LanternAuthFlow {

    private static final Pattern BEARER_TOKEN = Pattern.compile("Bearer ([A-Za-z0-9._~+/\\-]+=*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern OPAQUE_TOKEN = Pattern.compile("[^\\s,]+");

    private final AuthenticationFlowConfig authenticationConfig;

    public LanternAuthFlow() {
        this(new AuthenticationFlowConfig());
    }

    public LanternAuthFlow(AuthenticationFlowConfig authenticationConfig) {
        this.authenticationConfig = authenticationConfig;
    }

    public String getToken(HttpServletRequest request) {
        String tokenName = authenticationConfig.getTokenName();
        String token = null;
        if ("header".equals(authenticationConfig.getTokenSpot())) {
            Enumeration<String> headers = request.getHeaders(tokenName);
            if (headers == null || !headers.hasMoreElements()) {
                return null;
            }
            token = headers.nextElement();
            if (headers.hasMoreElements()) {
                return null;
            }
            if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(tokenName)) {
                Matcher bearer = BEARER_TOKEN.matcher(token);
                return bearer.matches() ? bearer.group(1) : null;
            }
        } else if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (tokenName.equals(cookie.getName())) {
                    if (token != null) {
                        return null;
                    }
                    token = cookie.getValue();
                }
            }
        }
        return token != null && OPAQUE_TOKEN.matcher(token).matches() ? token : null;
    }

    public String getResource(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
    }

    public void failed(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if ("header".equals(authenticationConfig.getTokenSpot())
                && HttpHeaders.AUTHORIZATION.equalsIgnoreCase(authenticationConfig.getTokenName())) {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        }
    }

    public boolean notAuthentication(String resource) {
        return authenticationConfig.getNotAuthentication() != null
                && authenticationConfig.getNotAuthentication().contains(resource);
    }

    public boolean userAuthentication(String resource) {
        return authenticationConfig.getUserAuthentication() != null
                && authenticationConfig.getUserAuthentication().contains(resource);
    }
}
