package com.lamp.lantern.plugins.api.auth;

import lombok.Data;

/**
 * @author laohu
 */
@Data
public class AuthenticationData {

    private String system;

    private String resource;

    private String token;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
