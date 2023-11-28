package com.lamp.lantern.plugins.springmvc.support;

import com.lamp.lantern.plugins.api.annotation.Injection;
import com.lamp.lantern.plugins.api.annotation.OrganizationInjection;
import com.lamp.lantern.plugins.api.annotation.UserInjection;
import com.lamp.lantern.plugins.api.mode.OrganizationInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author hahaha
 */
public class LanternAuthenticationUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserInjection.class) ||
                parameter.hasParameterAnnotation(OrganizationInjection.class) || parameter.hasParameterAnnotation(
                Injection.class);

    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.hasParameterAnnotation(UserInjection.class)) {
            return LanternContext.getContext().getUserInfo();
        } else if (parameter.hasParameterAnnotation(OrganizationInjection.class)
                && parameter.getParameterType().isAssignableFrom(OrganizationInfo.class)
        ) {
            return LanternContext.getContext().getValue("organization");
        }
        Injection injection = parameter.getParameterAnnotation(Injection.class);
        if (injection != null) {
            return LanternContext.getContext().getValue(injection.value());
        }
        throw new RuntimeException("");

    }
}
