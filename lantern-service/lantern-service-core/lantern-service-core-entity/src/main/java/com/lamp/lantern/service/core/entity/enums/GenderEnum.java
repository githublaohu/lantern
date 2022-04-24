package com.lamp.lantern.service.core.entity.enums;

public enum GenderEnum {

    MALE("MALE"),

    FEMALE("FEMALE"),

    OTHER("UNKNOW");

    private String gender;

    GenderEnum(String gender){
        this.gender = gender;
    }

};
