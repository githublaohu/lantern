package com.lamp.lantern.service.action.login;

import lombok.extern.slf4j.Slf4j;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableDubbo
public class LanternServiceActionLoginApplication {

    public static void main(String[] args) {
        try{
            SpringApplication.run(LanternServiceActionLoginApplication.class, args);
            log.info("{} 启动成功",LanternServiceActionLoginApplication.class.getSimpleName());
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
