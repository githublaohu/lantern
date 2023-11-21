package com.lamp.lantern.plugins.springmvc.support;

import com.lamp.lantern.plugins.api.annotation.Injection;
import com.lamp.lantern.plugins.api.annotation.OrganizationInjection;
import com.lamp.lantern.plugins.api.annotation.UserInjection;
import com.lamp.lantern.plugins.api.mode.OrganizationInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.util.Map;

public class LanternAuthenticationUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserInjection.class) ||
                        parameter.hasParameterAnnotation(OrganizationInjection.class) || parameter.hasParameterAnnotation(Injection.class);

    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        if(parameter.hasParameterAnnotation(UserInjection.class) ) {
            return LanternContext.getContext().getUserInfo();
        }else if(parameter.getParameterType().isAssignableFrom(OrganizationInfo.class)
                && parameter.hasParameterAnnotation(OrganizationInjection.class)){
            return null;
        }
        Injection injection = parameter.getMethod().getAnnotation(Injection.class);
        return null;

    }
}
