package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.AuthenticationType;
import com.lamp.lantern.plugins.api.mode.AuthenticationConfig;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author laohu
 */
public class AuthenticationManager {

    private final LanternAuthFlow lanternAuthFlow = new LanternAuthFlow();

    private AuthenticationDataService authOperation;

    private AuthenticationService authenticationService;

    private AuthenticationConfig authenticationConfig;


    public boolean authentication(HttpServletRequest request, HttpServletResponse response) {

        String resource = lanternAuthFlow.getResource(request);

        if (lanternAuthFlow.notAuthentication(resource)) {
            return true;
        }

        String token = lanternAuthFlow.getToken(request);
        if (Objects.isNull(token)) {
            lanternAuthFlow.failed(response);
            return false;
        }
        AuthenticationData authenticationData = new AuthenticationData();
        if (Objects.equals(authenticationConfig.getAuthenticationType(), AuthenticationType.USER)) {
            authenticationService.getUserInfo(authenticationData);
            return true;
        }

        AuthenticationServiceResult authenticationServiceResult =
                authenticationService.authentication(authenticationData);
        if (authenticationServiceResult.isSuccess()) {


        } else {
            lanternAuthFlow.failed(response);
        }
        return authenticationServiceResult.isSuccess();

    }


}
