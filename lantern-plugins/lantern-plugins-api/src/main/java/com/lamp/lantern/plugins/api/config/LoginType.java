package com.lamp.lantern.plugins.api.config;

import lombok.Getter;

public enum LoginType {

    PLATFORM("PLATFORM"),

    SECOND("SECOND"),

    THIRD("THIRD"),

    FIRST("FIRST"),

    QRCODE("QRCODE");

    @Getter
    private String type;

    private LoginType(String type) {
        this.type = type;
    }
}
