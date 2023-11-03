package com.lamp.lantern.plugins.core.login.exclusive;

import lombok.Data;

/**
 * @author lambert
 */
@Data
public class ExclusiveConfig {

    private ExclusiveMethod exclusiveMethod = ExclusiveMethod.KICK_SAME;

    private int allowNumber = 5;


    /**
     * 此Handler应该处于登录Handle最后
     * 1.登录成功后拒绝其他登录
     * 2.登录成功后下线其他登录
     * 3.登录成功后下线同一设备其他登录
     * 4.登录成功下线同样类型登录
     * 5.允许n台登录
     */
    enum ExclusiveMethod {
        /**
         * 同类型设备登录后,拒绝新的登录请求
         * Same as ALLOW_N(1)
         */
        REFUSE("同类型设备登录后,拒绝新的登录请求"),


        /**
         * 允许n台登录
         */
        ALLOW_NUMBER("允许n台登录"),
        /**
         * 登录后踢出所有在线设备
         */
        KICK_ALL("登录后踢出所有在线设备"),
        /**
         * 登录后踢出之前登录的同一设备
         */
        KICK_SAME("登录后踢出之前登录的同一设备"),
        /**
         * 登录后踢出同类型设备
         */
        KICK_TYPE("登录后踢出同类型设备"),

        NONE("不启用");

        int Number() {
            //判断自身是allow_n
            if (this == ExclusiveMethod.ALLOW_NUMBER) {
                return n;
            } else {
                throw new RuntimeException("ExclusiveMethod is not ALLOW_N");
            }
        }

        private int n = 5;

        boolean isKickFamily() {
            return this == ExclusiveMethod.KICK_ALL ||
                    this == ExclusiveMethod.KICK_SAME ||
                    this == ExclusiveMethod.KICK_TYPE;
        }

        boolean isRefuseFamily() {
            return this == ExclusiveMethod.REFUSE ||
                    this == ExclusiveMethod.ALLOW_NUMBER;
        }

        private String description;



        ExclusiveMethod(String description) {
            this.description = description;
        }
    }


}


