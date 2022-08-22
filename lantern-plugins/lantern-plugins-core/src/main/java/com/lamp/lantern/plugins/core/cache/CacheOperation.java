package com.lamp.lantern.plugins.core.cache;


/**
 * 一种本地缓存
 * 一种远程读取
 * @author laohu
 *
 */
public interface CacheOperation {

	
	public <T>T getUserInfo();
	
	public <T>T getOrganization();
	
	public <T>T getJurisdiction();
}
