package com.lamp.lantern.service.core.entity.enums;

import lombok.Getter;

public enum ExitWayEnum {
    ActiveExit("ActiveExit"),
    SessionExpire("SessionExpire"),
    ForceExit("ForceExit");

	@Getter
    private String exitWay;

    private ExitWayEnum(String exitWay){
        this.exitWay = exitWay;
    }
}
