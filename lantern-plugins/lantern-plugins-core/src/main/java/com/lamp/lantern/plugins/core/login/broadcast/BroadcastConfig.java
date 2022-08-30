package com.lamp.lantern.plugins.core.login.broadcast;

import lombok.Data;

@Data
public class BroadcastConfig {

	private String serviceAddress;
	
	private String topic;
	
	public enum BroadcastTypeEnums{
		
		DUBBO,
		
		ROCKETMQ,
		
		KAFKA;
	}
}
