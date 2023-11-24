/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.springmvc.support;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * @author hahaha
 */
public class LanternSpringMVCBehavior {


    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;


    @PostConstruct
    public void init() {
        List<HandlerMethodArgumentResolver> handlers = requestMappingHandlerAdapter.getArgumentResolvers();
        if (Objects.isNull(handlers)) {
            return;
        }
        List<HandlerMethodArgumentResolver> newHandlerMethodArgumentResolver = new ArrayList<>(handlers.size());
        for (HandlerMethodArgumentResolver handlerMethodArgumentResolver : handlers) {
            if (handlerMethodArgumentResolver.getClass().equals(RequestResponseBodyMethodProcessor.class)) {
                handlerMethodArgumentResolver =
                        new LanternRequestResponseBodyMethodProcessor(
                                (RequestResponseBodyMethodProcessor) handlerMethodArgumentResolver);
            }
            newHandlerMethodArgumentResolver.add(handlerMethodArgumentResolver);
        }
        requestMappingHandlerAdapter.setArgumentResolvers(newHandlerMethodArgumentResolver);
    }
}
