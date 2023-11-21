package com.lamp.lantern.plugins.api.enums;

import lombok.Getter;

public enum LoginStatusEnum {
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

	@Getter
    private String status;

    private LoginStatusEnum(String status){
        this.status = status;
    }
}
