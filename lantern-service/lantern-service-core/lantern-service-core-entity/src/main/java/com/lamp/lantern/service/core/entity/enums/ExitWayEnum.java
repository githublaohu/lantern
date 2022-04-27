package com.lamp.lantern.service.core.entity.enums;

public enum ExitWayEnum {
    ActiveExit("ActiveExit"),
    SessionExpire("SessionExpire"),
    ForceExit("ForceExit");

    private String exitWay;

    private ExitWayEnum(String exitWay){
        this.exitWay = exitWay;
    }
}
