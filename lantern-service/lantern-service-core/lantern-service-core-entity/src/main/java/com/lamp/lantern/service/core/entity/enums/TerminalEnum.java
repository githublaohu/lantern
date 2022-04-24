package com.lamp.lantern.service.core.entity.enums;

public enum TerminalEnum {
    MB_BROWSER("MB_BROWSER"),      // 手机浏览器
    PC_BROWSER("PC_BROWSER"),      // 电脑浏览器
    MB_APP("MB_APP"),              // 手机APP
    MB_APPLET("MB_APPLET")         // 手机小程序
    ;


    public String terminalName;

    private TerminalEnum(String terminalName){
        this.terminalName = terminalName;
    }




}
