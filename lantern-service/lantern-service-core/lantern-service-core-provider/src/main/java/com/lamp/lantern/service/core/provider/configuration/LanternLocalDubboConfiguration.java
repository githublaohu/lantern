package com.lamp.lantern.service.core.provider.configuration;

import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.service.core.provider.autoconfigure.LanternDatabaseAuthenticationAutoConfiguration;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import com.lamp.lantern.service.core.provider.service.impl.DBAuthenticationServiceImpl;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Imported after local defaults are available; reuses the host's MyBatis data source.
 */
@Configuration
@MapperScan(basePackageClasses = UserInfoMapper.class, annotationClass = Mapper.class)
@EnableDubbo(scanBasePackageClasses = LanternDatabaseAuthenticationAutoConfiguration.class)
public class LanternLocalDubboConfiguration {

    @Bean("DBAuthentication")
    @ConditionalOnMissingBean(name = "DBAuthentication")
    public DBAuthenticationServiceImpl databaseAuthentication() {
        return new DBAuthenticationServiceImpl();
    }

    @Bean
    @DubboService(interfaceClass = AuthenticationService.class)
    public AuthenticationService lanternAuthenticationProvider(@Qualifier("DBAuthentication") AuthenticationService database) {
        return database;
    }

    @Bean
    @DubboService(interfaceClass = AuthenticationPolicyService.class)
    public AuthenticationPolicyService lanternPolicyProvider(@Qualifier("DBAuthentication") AuthenticationPolicyService database) {
        return database;
    }

    @Bean("lanternDubboAuthentication")
    @DubboReference
    public ReferenceBean<AuthenticationService> lanternDubboAuthentication() {
        return new ReferenceBean<>();
    }

    @Bean("lanternDubboPolicy")
    @DubboReference
    public ReferenceBean<AuthenticationPolicyService> lanternDubboPolicy() {
        return new ReferenceBean<>();
    }
}
