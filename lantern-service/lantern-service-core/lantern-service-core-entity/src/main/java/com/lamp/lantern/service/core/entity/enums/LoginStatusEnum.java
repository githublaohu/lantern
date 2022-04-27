package com.lamp.lantern.service.core.entity.enums;

public enum LoginStatusEnum {
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

    private String status;

    private LoginStatusEnum(String status){
        this.status = status;
    }
}
