package com.lamp.lantern.serivce.action.login;


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
public class LanternServiceActionLoginApplication
{
    public static void main( String[] args )
    {
       try{

           SpringApplication.run(LanternServiceActionLoginApplication.class, args);
           log.info("{} 启动成功", LanternServiceActionLoginApplication.class.getSimpleName());

       } catch (Exception e){

           log.error(e.getMessage(), e);
       }
    }
}
