package com.lamp.lantern.plugins.api.auth;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author laohu
 */
@Data
public class AuthenticationData implements Serializable {

    private String systemId;

    private Long userId;

    private String resource;

    private String token;

    private UserInfo userInfo;

    private Long time;

}
