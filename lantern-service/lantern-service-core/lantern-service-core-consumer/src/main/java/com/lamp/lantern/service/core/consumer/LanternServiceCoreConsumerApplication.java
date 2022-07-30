package com.lamp.lantern.service.core.consumer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class LanternServiceCoreConsumerApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(LanternServiceCoreConsumerApplication.class, args);
            log.info("{} 启动成功", LanternServiceCoreConsumerApplication.class.getSimpleName());
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }
}
