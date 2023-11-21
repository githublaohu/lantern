package com.lamp.lantern.service.action.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@SpringBootApplication
@EnableSwagger2
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
