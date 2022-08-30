package com.lamp.lantern.plugins.api.config;

import lombok.Data;

@Data
public class AuthChannelCofing {

	/**
	 * 登录方式
	 */
	private String loginType;

	/**
	 * 登录渠道
	 */
	private String authChannel;

	private String className;

	private String beanName;

	private String beanClass;

	/**
	 * 服务器地址
	 */
	private String serviceAddress;

	/**
	 * appId
	 */
	private String appId;

	/**
	 * ak
	 */
	private String accessKey;

	/**
	 * sk
	 */
	private String secretAccessKey;
	
	private String privateKey;
	
	private String publicKey;
	
	private String format = "json";
	
    private String charset = "UTF-8";
    
    
    private String signType = "RSA2";
}
