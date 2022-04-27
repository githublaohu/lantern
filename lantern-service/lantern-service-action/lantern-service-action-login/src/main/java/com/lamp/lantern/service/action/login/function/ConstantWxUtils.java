package com.lamp.lantern.service.action.login.function;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "wx.open")
@Component
@Data
public class ConstantWxUtils {

    private String appId;

    private String appSecret;

    private String redirectUrl;
}
