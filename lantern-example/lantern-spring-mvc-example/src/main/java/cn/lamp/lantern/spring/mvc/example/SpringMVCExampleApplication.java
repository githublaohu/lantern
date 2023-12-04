package cn.lamp.lantern.spring.mvc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class SpringMVCExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMVCExampleApplication.class, args);
    }
}
