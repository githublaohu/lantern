package com.lamp.lantern.plugins.api.auth.config;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Data;

@Data
public class RedisCacheConfig {

    private String redisCacheUrl;

    private String redisCacheSystemName;
}
