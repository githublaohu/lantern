package com.lamp.lantern.plugins.springmvc.support;


import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.login.LanternContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationManager authenticationManager;

    public AuthenticationInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        AuthenticationServiceResult authenticationServiceResult =  authenticationManager.authentication(request, response);
        if (authenticationServiceResult.isSuccess()) {
            LanternContext.getContext().setUserInfo(authenticationServiceResult.getUserInfo());
            return true;
        }
        return false;
    }
}