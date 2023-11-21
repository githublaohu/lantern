package com.lamp.lantern.plugins.core.login.config;

import com.lamp.lantern.plugins.api.service.LanternUserInfoService;
import lombok.Data;

@Data
public class LanternUserInfoConfig {
    private String beanName;

    private String beanClass;

    private LanternUserInfoService lanternUserInfoService;

}
