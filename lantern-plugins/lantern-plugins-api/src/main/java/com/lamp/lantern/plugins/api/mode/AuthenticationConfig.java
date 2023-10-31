package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationType;
import java.util.Set;
import lombok.Data;

/**
 * @author laohu
 */
@Data
public class AuthenticationConfig {

	private String systemId;

	private String systemName;

	private Set<String> notAuthentication;

	private Set<String> userAuthentication;

	private int authentication;

	private boolean resourcesAuthentication = false;

	private String redirectData;

	private String tokenSpot;

	private String tokenName;

	private String redirectSpot;

	private AuthenticationType authenticationType;

	private AuthenticationDataService authenticationDataService;

	private Long dataSyncInterval;

	private AuthenticationService authenticationService;

}
