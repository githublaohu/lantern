package com.lamp.lantern.service.core.entity.enums;

import lombok.Getter;

public enum TerminalEnum {
    MbBrowser("MbBrowser"),
    PcBrowser("PcBrowser"),
    TriApp("TriApp"),
    TriApplet("TriApplet");

	@Getter
    private String terminal;

    private TerminalEnum(String terminal){
        this.terminal = terminal;
    }
}
