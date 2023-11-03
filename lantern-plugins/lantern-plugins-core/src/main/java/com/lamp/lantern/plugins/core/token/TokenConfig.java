/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
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
//	private Long effective;

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
