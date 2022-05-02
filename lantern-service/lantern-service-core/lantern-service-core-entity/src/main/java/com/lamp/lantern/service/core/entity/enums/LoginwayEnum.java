package com.lamp.lantern.service.core.entity.enums;

public enum LoginWayEnum {
    AccountLogin("AccountLogin"),
    VerifyCodeLogin("VerifyCodeLogin"),
    ScanCodeLogin("ScanCodeLogin"),
    AppLogin("AppLogin");

    private String loginWay;

    private LoginWayEnum(String loginWay){
        this.loginWay = loginWay;
    }
}
