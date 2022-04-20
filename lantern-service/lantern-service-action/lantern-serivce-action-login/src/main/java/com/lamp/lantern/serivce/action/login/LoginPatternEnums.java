package com.lamp.lantern.serivce.action.login;

public enum LoginPatternEnums {

	PARTY_ACCOUNT("party","account"),

	PARTY_PHONE("party","phone"),

	PARTY_EMAIL("party","email"),

	PARTY_VERIFYCODE("party","verifycode"),

	PARTY_TOKEN("party", "token"),

	PLATFORM_WECHAT("platfrom","wechat"),

	TRIPARTITE_APPLET("tripartite","applet");

	private String party;

	private String channel;

	private LoginPatternEnums(String party, String channel) {
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
