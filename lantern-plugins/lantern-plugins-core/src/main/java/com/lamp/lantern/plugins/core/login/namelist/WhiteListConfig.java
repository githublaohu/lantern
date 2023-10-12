package com.lamp.lantern.plugins.core.login.namelist;

import lombok.Data;

@Data
public class WhiteListConfig {

	/**
	 * 传入:
	 * "entity" 直接识别实体类的状态
	 *
	 */
	private String whiteListSourceType;

	/**
	 * 是白名单，还是黑名单
	 */
	private WhiteListType whiteListType;

	enum WhiteListType{
		WHITE("白名单"),
		BLACK("黑名单");

		WhiteListType(String description){}
	}

}
