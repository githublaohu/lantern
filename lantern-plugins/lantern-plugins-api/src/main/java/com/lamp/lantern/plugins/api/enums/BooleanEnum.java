package com.lamp.lantern.plugins.api.enums;

import lombok.Getter;

public enum BooleanEnum {

    True("True"),

    False("False");

	@Getter
    private String flag;

    BooleanEnum(String flag){
        this.flag = flag;
    }

    BooleanEnum(){}
}
