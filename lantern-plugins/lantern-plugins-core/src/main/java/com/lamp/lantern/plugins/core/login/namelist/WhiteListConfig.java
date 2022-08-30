package com.lamp.lantern.plugins.core.login.namelist;

import lombok.Data;

@Data
public class WhiteListConfig {

	/**
	 * entity 直接识别实体类的状态
	 */
	private String whiteListSourceType;

	/**
	 * 是白名单，还是黑名单
	 */
	private String whiteListType;

}
