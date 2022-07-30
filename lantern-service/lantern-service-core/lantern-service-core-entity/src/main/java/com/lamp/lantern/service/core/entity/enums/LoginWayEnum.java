package com.lamp.lantern.service.core.entity.enums;

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
