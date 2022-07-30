package com.lamp.lantern.plugins.core.cache;


public interface CacheOperation {

	
	public <T>T getUserInfo();
	
	public <T>T getOrganization();
	
	public <T>T getJurisdiction();
}
