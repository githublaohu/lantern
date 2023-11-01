package com.lamp.lantern.plugins.api.config;

import io.lettuce.core.api.StatefulRedisConnection;
import jdk.jfr.Unsigned;
import lombok.Data;

@Data
public class AuthChannelConfig {

	/**
	 * 登录方式
	 */
	private LoginType loginType;

	/**
	 * 登录渠道
	 */
	private String authChannel;

	/**
	 * used internally
	 */
	private String simpleClassName;

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
	 * accessKey
	 */
	private String accessKey;

	/**
	 * secretAccessKey
	 */
	private String secretAccessKey;

	private String redirectUri;

	private String scope;
	
	private String privateKey;
	
	private String publicKey;
	
	private String format = "json";
	
    private String charset = "UTF-8";

	private String redisUrl;

	private Integer qrcodeExpire;

	private String qrcodeKeyPrefix = "LanternQrcode:";
    
    private String signType = "RSA2";
}
