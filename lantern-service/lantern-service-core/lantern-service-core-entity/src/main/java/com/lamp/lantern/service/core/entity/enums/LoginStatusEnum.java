package com.lamp.lantern.service.core.entity.enums;

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
