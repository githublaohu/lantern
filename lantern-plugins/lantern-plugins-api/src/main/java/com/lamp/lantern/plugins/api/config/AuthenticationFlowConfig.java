package com.lamp.lantern.plugins.api.config;

import java.util.Set;
import lombok.Data;

/**
 * @author laohu
 */

@Data
public class AuthenticationFlowConfig {

    private String systemId;

    private String systemName;

    private Set<String> notAuthentication;

    private Set<String> userAuthentication;

    private int authentication;

    private boolean resourcesAuthentication = false;

    private String redirectData;

    /**
     * token位置 "cookie" or "header"
     */
    private String tokenSpot;

    private String tokenName;

    private String redirectSpot;
}
