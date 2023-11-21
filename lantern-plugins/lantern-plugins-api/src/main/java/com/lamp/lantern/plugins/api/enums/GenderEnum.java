package com.lamp.lantern.plugins.api.enums;

import lombok.Getter;

public enum GenderEnum {

    MALE("MAlE"),

    FEMALE("FEMALE"),

    UNKNOWN("UNKNOWN");

	@Getter
    private String gender;

    private GenderEnum(String gender){
        this.gender = gender;
    }


}
