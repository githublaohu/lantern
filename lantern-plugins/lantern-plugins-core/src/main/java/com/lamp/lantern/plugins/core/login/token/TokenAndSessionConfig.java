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
package com.lamp.lantern.plugins.core.login.token;

import com.lamp.lantern.plugins.core.token.TokenCreateMode;
import lombok.Data;

@Data
public class TokenAndSessionConfig {

    private TokenCreateMode tokenCreateMode;

    private String tokenName ;
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
