package com.lamp.lantern.plugins.core.login.token;

import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.plugins.core.token.TokenCreateMode;
import lombok.Data;

@Data
public class TokenAndSessionConfig {

    private TokenCreateMode tokenCreateMode;

    private String tokenName;
    /**
     * "cookie" or "header"
     */
    private String dataPosition;

//    private String tokenKey;

    private String cookieDomain;

    private int cookieMaxAge = -1;

    private String cookiePath = "/";

    private boolean cookieSecure = false;

    private boolean cookieHttpOnly = true;

    private int tokenExpire = 60 * 60 * 24 * 7;


}
