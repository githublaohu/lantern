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
package com.lamp.lantern.plugins.auth.second;

import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@ConfigurationProperties("SecondConfig")
@Component("second-lantern")
public class SecondAuthOperate extends AbstractAuthService {

    private Map<String, SecondLoginConfig> secondLoginConfigMap = new HashMap<>();

    /**
     * 认证用户的token
     *
     * @return
     */
    @Override
    public AuthResultObject auth(UserInfo userInfo) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // 第一步获得域名
        String domain = request.getHeader("Host");
        // 找第二方呀
        SecondLoginConfig secondLoginConfig = this.secondLoginConfigMap.get(domain);
        if (Objects.isNull(secondLoginConfig)) {
            return null;
        }
        String token = null;
        if (Objects.equals("header", secondLoginConfig.getTokenLocal())) {
            token = request.getHeader(secondLoginConfig.getTokenName());

        } else if (Objects.equals("cooke", secondLoginConfig.getTokenLocal())) {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), secondLoginConfig.getTokenName())) {
                    token = cookie.getName();
                }
            }
        } else if (Objects.equals("path", secondLoginConfig.getTokenLocal())) {
            request.getRequestURI();
        }
        if (Objects.isNull(token)) {
            //重定向
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            try {
                response.sendRedirect(secondLoginConfig.getSecondAddress());
            } catch (IOException e) {
                log.error(e.getMessage());
                AuthResultObject resultObject = AuthResultObject.create();
                resultObject.setErrorMessage(e.getMessage());
                return resultObject;
            }
            String s = "";
        }
        //
        if (request.isRequestedSessionIdValid()) {
            return null;
        }
        // 使用ligth 调用二方接口，要求二方解决返回用户数据


        return null;
    }

    @Override
    public AuthResultObject getUserInfo(UserInfo userInfo) {
        return null;
    }


}
