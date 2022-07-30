package com.lamp.lantern.service.core.entity.enums;

import lombok.Getter;

public enum GenderEnum {

    MALE("MAlE"),

    FAMALE("FAMALE"),

    UNKNOWN("UNKOWN");

	@Getter
    private String gender;

    private GenderEnum(String gender){
        this.gender = gender;
    }


}
