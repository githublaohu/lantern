package com.lamp.lantern.service.core.entity.enums;

public enum ExitModeEnum {

    USER_EXIT("USER_EXIT"),
    SESSION_EXPIRE("SESSION_EXPIRE"),
    FORCE_EXIT("FORCE_EXIT");

    public String exitModeName;

    private ExitModeEnum(String exitModeName){
        this.exitModeName = exitModeName;
    }

}
