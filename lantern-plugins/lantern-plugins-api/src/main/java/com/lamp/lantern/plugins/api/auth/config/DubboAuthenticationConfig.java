package com.lamp.lantern.plugins.api.auth.config;

import lombok.Data;

@Data
public class DubboAuthenticationConfig {

    String applicationName;

    /**
     * 如果设置了url，则使用直连，不使用注册中心
     * “dubbo://”
     */
    String url;

    /**
     * 注册中心地址
     * “zookeeper://”
     */
    String registryAddress;

    Integer timeout;

    String group;

    String version;
}
