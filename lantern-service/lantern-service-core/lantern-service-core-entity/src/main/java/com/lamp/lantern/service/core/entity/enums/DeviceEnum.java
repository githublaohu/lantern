package com.lamp.lantern.service.core.entity.enums;

public enum DeviceEnum {

    APPLE("APPLE"),
    XIAOMI("XIAOMI"),
    VIVO("VIVO"),
    OPPO("OPPO"),
    HUAWEI("HUAWEI"),
    ALLENWARE("ALLENWARE"),
    OTHER("OTHER");


    public String brandName;

    private DeviceEnum(String brandName){
        this.brandName = brandName;
    }

}
