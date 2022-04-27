package com.lamp.lantern.service.core.entity.enums;

public enum StatusEnum {

    ACTIVE("ACTIVE"),

    INACTIVE("ACTIVE");

    private String status;

    private StatusEnum(String status){
        this.status = status;
    }

}
