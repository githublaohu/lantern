package com.lamp.lantern.service.core.entity.enums;

public enum TerminalEnum {
    MbBrowser("MbBrowser"),
    PcBrowser("PcBrowser"),
    TriApp("TriApp"),
    TriApplet("TriApplet");

    private String terminal;

    private TerminalEnum(String terminal){
        this.terminal = terminal;
    }
}
