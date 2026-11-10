package com.lamp.lantern.plugins.api.auth;

public interface AuthenticationPolicyService {

    /**
     * Returns the active policy for an exact method and route template, or null if none applies.
     */
    AuthenticationPolicy findPolicy(Long systemId, String method, String route);
}
