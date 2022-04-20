package com.lamp.lantern.service.core.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 各表查询所有数据接口
 *
 */
@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class LanternServiceCoreProviderApplication
{
    public static void main(String[] args )
    {
        try {
            SpringApplication.run(LanternServiceCoreProviderApplication.class, args);
            log.info("{} 启动成功",LanternServiceCoreProviderApplication.class.getSimpleName());
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
