package com.lamp.lantern.serivce.action.logon;

public enum LoginPatternEnums {

	PARTY_EMAIL("party","email");
	
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
