package com.lamp.lantern.service.core.entity.enums;

public enum LoginPatternEnum {


    PARTY_ACCOUNT("party","account"),

    PARTY_PHONE("party","phone"),

    PARTY_EMAIL("party","email"),

    PARTY_VERIFYCODE("party","verifycode"),

    PARTY_TOKEN("party", "token"),

    PLATFORM_WECHAT("platfrom","wechat"),

    TRIPARTITE_WEIXIN_SCAN("tripartite","weixin_scan"),

    TRIPARTITE_APPLET("tripartite","applet");


    private String party;

    private String channel;

    private LoginPatternEnum(String party, String channel) {
        this.party = party;
        this.channel = channel;
    }

    public String getParty() {
        return party;
    }

    public String getChannel() {
        return channel;
    }

}
