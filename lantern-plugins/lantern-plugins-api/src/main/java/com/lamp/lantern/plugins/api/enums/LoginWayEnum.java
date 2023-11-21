package com.lamp.lantern.plugins.api.enums;

import lombok.Getter;

public enum LoginWayEnum {
    AccountLogin("AccountLogin"),
    VerifyCodeLogin("VerifyCodeLogin"),
    ScanCodeLogin("ScanCodeLogin"),
    AppLogin("AppLogin");

	@Getter
    private String loginWay;

    private LoginWayEnum(String loginWay){
        this.loginWay = loginWay;
    }
}
