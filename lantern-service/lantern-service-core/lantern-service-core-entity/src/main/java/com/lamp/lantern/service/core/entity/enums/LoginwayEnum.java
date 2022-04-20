package com.lamp.lantern.service.core.entity.enums;

public enum LoginwayEnum {

    ACCOUNT_PASSWORD("ACCOUNT_PASSWORD"),
    PHONE_VERIFYCODE("PHONE_VERIFYCODE"),
    TRI_SCANCODE("TRI_SCANCODE"),
    TRI_APP("TRI_APP");


    public String loginwayName;

    private LoginwayEnum(String loginwayName){
        this.loginwayName = loginwayName;
    }
}
