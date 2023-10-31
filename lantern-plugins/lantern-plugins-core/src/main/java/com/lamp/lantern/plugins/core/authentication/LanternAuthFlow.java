package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.mode.AuthenticationConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author laohu
 */
public class LanternAuthFlow {

    private AuthenticationConfig authenticationConfig;


    public String getToken(HttpServletRequest request) {
        return "";
    }

    public String getResource(ServletRequest request) {
        return null;
    }

    public void failed(ServletResponse response) {

    }

    public boolean notAuthentication(String resource) {
        return false;
    }

    public boolean userAuthentication(String resource) {
        return false;
    }
}
