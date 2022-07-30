package com.lamp.lantern.service.core.entity.enums;

import lombok.Getter;

public enum DeviceEnum {

    APPLE("APPLE"),
    XIAOMI("XIAOMI"),
    VIVO("VIVO"),
    OPPO("OPPO"),
    HUAWEI("HUAWEI"),
    SAMSUNG("SAMSUNG"),
    LENOVO("LENOVO"),
    DELL("DELL"),
    ALLENWARE("ALLENWARE"),
    OTHER("OTHER");

	@Getter
    private String device;

    private DeviceEnum(String device){
        this.device = device;
    }

}
