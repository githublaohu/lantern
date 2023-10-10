package com.lamp.lantern.plugins.core.login.exclusive;

import lombok.Data;

/**
 * @author lambert
 */
@Data
public class ExclusiveConfig {

    private Method method = Method.KICK_SAME;


    /**
     * 此Handler应该处于登录Handle最后
     * 1.登录成功后拒绝其他登录
     * 2.登录成功后下线其他登录
     * 3.登录成功后下线同一设备其他登录
     * 4.登录成功下线同样类型登录
     * 5.允许n台登录
     */
    enum Method {
        /**
         * 同类型设备登录后,拒绝新的登录请求
         * Same as ALLOW_N(1)
         */
        REFUSE,
        /**
         * 登录后踢出所有在线设备
         */
        KICK_ALL,
        /**
         * 登录后踢出之前登录的同一设备
         */
        KICK_SAME,
        /**
         * 登录后踢出同类型设备
         */
        KICK_TYPE,
        /**
         * 允许n台登录
         */
        ALLOW_N(5),

        /**
         * 不开启
         */
        NONE;
        public static Method ALLOW_N(int n) {
            Method method = Method.ALLOW_N;
            method.n = n;
            return method;
        }
        int N(){
            //判断自身是allow_n
            if (this == Method.ALLOW_N) {
                return n;
            }
            else {
                throw new RuntimeException("Method is not ALLOW_N");
            }
        }
        private int n = 5;
        Method(int i) {
            this.n = i;
        }
        Method() {
        }
    }


}


//REFUSE - 拒绝其他登录
//authBefore:
//
//如果任何设备已登录,返回拒绝登录的结果
//authAfter:
//
//登记登录状态
//KICK_ALL - 下线其他登录
//authBefore:
//
//无需特殊处理
//authAfter:
//
//遍历Redis中的设备登录信息,下线其他登录会话
//登记登录状态
//KICK_SAME - 下线同一设备的其他登录
//authBefore:
//
//无需特殊处理
//authAfter:
//
//如果设备已登录,下线旧登录会话
//登记登录状态
//KICK_TYPE - 下线同类型登录
//authBefore:
//
//无需特殊处理
//authAfter:
//
//判断设备类型
//遍历Redis设备信息,下线同类型设备的其他登录
//ALLOW_N - 允许N台登录
//authBefore:
//
//检查已登录设备数是否超过N
//如果超过,返回拒绝登录结果
//authAfter:
//
//无需特殊处理