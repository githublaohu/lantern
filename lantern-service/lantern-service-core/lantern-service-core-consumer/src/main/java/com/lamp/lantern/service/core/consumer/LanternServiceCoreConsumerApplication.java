package com.lamp.lantern.service.core.consumer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableDubbo
public class LanternServiceCoreConsumerApplication
{
    public static void main( String[] args )
    {
        try {
            SpringApplication.run(LanternServiceCoreConsumerApplication.class, args);
            log.info("{} 启动成功", LanternServiceCoreConsumerApplication.class.getSimpleName());
        } catch (Exception e){
            System.out.println("error");
            log.error(e.getMessage(),e);
        }
    }
}
