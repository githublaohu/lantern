package com.lamp.lantern.plugins.api.annotation;


import com.lamp.lantern.plugins.api.config.LoginType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lambert
 * 用于指定一个AuthService的LoginType和AuthChannel
 * LoginType使用枚举类
 * AuthChannel使用字符串，如`Alipay`
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthTypeChannel {
    LoginType loginType() ;

    String authChannel() default "";

}
