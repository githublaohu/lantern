package com.lamp.lantern.plugins.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheOperation implements CacheOperation{

	private Map<String , Object> userMap = new ConcurrentHashMap<>();
	
	private Map<String/** 资源类型 **/ , Map<String/*资源id*/, Map<String,Object>>>  rouseceMap = new ConcurrentHashMap<>();
	
	
	public void addUserInfo(String key , T userInfo) {
		userMap.put(key, userInfo);
	}
	
	public void deleteUserInfo(String key) {
		userMap.remove(key);
	}
	
	@Override
	public <T> T getUserInfo() {
		return null;
	}

	@Override
	public <T> T getOrganization() {
		return null;
	}

	@Override
	public <T> T getJurisdiction() {
		return null;
	}

}
