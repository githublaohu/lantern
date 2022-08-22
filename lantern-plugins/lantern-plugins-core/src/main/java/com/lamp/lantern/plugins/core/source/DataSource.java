package com.lamp.lantern.plugins.core.source;

public interface DataSource {

	public <T> T getData(String key, Class<?> clazz);

	public <T> T getData(String key);
	
	public <T> T getData(RelationStruct relationStruct);
	
	public <T> T getUserRelationData(String key);
}
