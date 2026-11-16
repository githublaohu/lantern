package com.lamp.lantern.plugins.api.auth;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author laohu
 */
@Data
public class AuthenticationData implements Serializable {

    private static final long serialVersionUID = -908863518831446342L;

    private String productId;

    private Long systemId;

    private Long userId;

    private String resource;

    private Long resourceId;

    private String token;

    private UserInfo userInfo;

    private Long time;

}
