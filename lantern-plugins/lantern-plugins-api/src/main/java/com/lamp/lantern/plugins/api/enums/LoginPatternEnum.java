package com.lamp.lantern.plugins.api.enums;

public enum LoginPatternEnum {


    PARTY_ACCOUNT("party","account"),

    PARTY_PHONE("party","phone"),

    PARTY_EMAIL("party","email"),

    PARTY_VERIFY_CODE("party","verifycode"),

    PARTY_TOKEN("party", "token"),

    PLATFORM_WECHAT("platform","wechat"),

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
