package com.lamp.lantern.plugins.api.auth;

import java.io.Serializable;
import lombok.Value;

@Value
public class AuthenticationPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    Long resourceId;
    Mode mode;

    public enum Mode {
        PUBLIC,
        USER,
        ROLE
    }
}
