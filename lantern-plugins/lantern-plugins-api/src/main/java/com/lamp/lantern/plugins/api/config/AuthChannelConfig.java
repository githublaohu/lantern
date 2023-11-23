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
package com.lamp.lantern.plugins.api.config;

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
