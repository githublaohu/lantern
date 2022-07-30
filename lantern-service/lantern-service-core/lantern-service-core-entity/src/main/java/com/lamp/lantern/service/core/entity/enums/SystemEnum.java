package com.lamp.lantern.service.core.entity.enums;

import lombok.Getter;

public enum SystemEnum {
    MACOS("MACOS"),
    WINDOWS("WINDOWS"),
    LINUX("LINUX"),
    IOS("IOS"),
    ANDRIOD("ANDRIOD"),
    HARMONY("HARMONY"),
    OTHER("OTHER");

	@Getter
    private String system;

    private SystemEnum(String system){
        this.system = system;
    }
}
