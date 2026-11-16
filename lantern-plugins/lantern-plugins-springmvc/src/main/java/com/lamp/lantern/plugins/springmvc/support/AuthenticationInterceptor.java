package com.lamp.lantern.plugins.springmvc.support;


import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.login.LanternContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hahaha
 */
public class AuthenticationInterceptor implements AsyncHandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationInterceptor.class);
    private final AuthenticationManager authenticationManager;

    public AuthenticationInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LanternContext.removeContext();
        Object pattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        try {
            AuthenticationServiceResult result = authenticationManager.authentication(
                    request, response, pattern instanceof String ? (String) pattern : null);
            if (result.isSuccess() && result.getUserInfo() != null) {
                LanternContext.getContext().setUserInfo(result.getUserInfo());
            }
            return result.isSuccess();
        } catch (RuntimeException exception) {
            LOGGER.warn("Lantern request authentication failed: {}", exception.getClass().getSimpleName());
            LanternContext.removeContext();
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        LanternContext.removeContext();
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LanternContext.removeContext();
    }
}
