package com.lamp.lantern.service.core.entity.enums;

import lombok.Getter;

public enum StatusEnum {

    ACTIVE("ACTIVE"),

    INACTIVE("ACTIVE");

	@Getter
    private String status;

    private StatusEnum(String status){
        this.status = status;
    }

}
