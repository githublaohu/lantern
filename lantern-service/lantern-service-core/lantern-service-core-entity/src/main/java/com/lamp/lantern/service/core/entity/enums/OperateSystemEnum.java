package com.lamp.lantern.service.core.entity.enums;

public enum OperateSystemEnum {

    MACOS("MACOS"),
    WINDOWS("WINDOWS"),
    LINUX("LINUX"),
    IOS("IOS"),
    ANDRIOD("ANDRIOD"),
    HARMONY("HARMONY"),
    OTHER("OTHER");


    public String systemName;

    private OperateSystemEnum(String systemName){

        this.systemName = systemName;

    }

}
