package com.lamp.lantern.service.core.entity.enums;

public enum BooleanEnum {

    True("True"),

    False("False");

    private String flag;

    BooleanEnum(String flag){
        this.flag = flag;
    }

    BooleanEnum(){}
}
