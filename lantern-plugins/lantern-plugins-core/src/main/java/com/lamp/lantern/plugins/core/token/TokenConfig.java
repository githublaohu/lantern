package com.lamp.lantern.plugins.core.token;

import lombok.Data;

@Data
public class TokenConfig {

	/**
	 * 
	 */
	private TokenCreateMode tokenCreateMode;

	/**
	 * 有效时间
	 */
	private Long effective;

	/**
	 * 算法
	 */
	private String algorithm;

	/**
	 * 密钥
	 */
	private String key;

	/**
	 * token存放位置
	 */
	private String position;

	/**
	 * token名
	 */
	private String tokenName;
}
