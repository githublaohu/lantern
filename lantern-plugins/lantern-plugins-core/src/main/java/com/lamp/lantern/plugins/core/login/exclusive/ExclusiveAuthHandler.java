package com.lamp.lantern.plugins.core.login.exclusive;


import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.LanternContext;
import io.lettuce.core.ScriptOutputType;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 此Handler应该处于登录Handle最后
 * 1.登录成功后拒绝其他登录            REFUSE
 * 2.登录成功后下线其他登录            KICK_ALL
 * 3.登录成功后下线同一设备其他登录      KICK_SAME
 * 4.登录成功下线同样类型登录           KICK_TYPE
 * 5.允许n台登录                     ALLOW_N
 */
public class ExclusiveAuthHandler extends AbstractAuthHandler<ExclusiveConfig> {

    @Override
    public ResultObject<String> authBefore(UserInfo userInfo) {

        if (config.getMethod() != ExclusiveConfig.Method.REFUSE &&
                config.getMethod() != ExclusiveConfig.Method.ALLOW_N) {
            return null;
        }
        //REFUSE模式下,如果该设备已经登录,则拒绝登录
        Long count = connection.sync().hlen(this.systemName + "-" + userInfo.getUiId().toString());
        if (config.getMethod() == ExclusiveConfig.Method.REFUSE && count > 0) {
            return new ResultObject<String>(10001, "该设备已经登录");
        }
        if (config.getMethod() == ExclusiveConfig.Method.ALLOW_N) {
            //ALLOW_N模式下,如果该设备已经登录,则判断是否超过允许登录数
            if (count >= config.getMethod().N()) {
                return new ResultObject<String>(10101, "登录设备已达上限");
            }
        }
        return null;
    }

    @Override
    public ResultObject<String> authAfter(UserInfo userInfo) {

        String hashKey = getHashKey();
        String key = this.systemName + "-" + userInfo.getUiId().toString();


        if (config.getMethod() != ExclusiveConfig.Method.KICK_SAME &&
                config.getMethod() != ExclusiveConfig.Method.KICK_ALL &&
                config.getMethod() != ExclusiveConfig.Method.KICK_TYPE) {
            //如果不是KICK类方法, 则不需要处理
            connection.async().hset(key, hashKey, getDeviceType());
            return null;
        }

        //TODO 怎么踢
        if (config.getMethod() == ExclusiveConfig.Method.KICK_ALL) {
            //踢出所有在线设备
            connection.sync().del(key);
        } else if (config.getMethod() == ExclusiveConfig.Method.KICK_SAME) {
            //踢出同一设备
            connection.sync().hdel(key, hashKey);
        } else if (config.getMethod() == ExclusiveConfig.Method.KICK_TYPE) {
            //踢出同类型设备

            String luaScript =
                    "local deviceType = ARGV[1] " +
                            "local Key = KEYS[1] " +
                            "local fields = redis.call('HGETALL', Key) " +
                            "for i, v in ipairs(fields) do " +
                            "if i % 2 == 0 then " +
                            "local fieldValue = fields[i] " +
                            "if fieldValue == deviceType then " +
                            "redis.call('HDEL', Key, fields[i-1]) " +
                            "end " +
                            "end " +
                            "end";

            String[] keys = new String[]{key};
            connection.sync().eval(luaScript, ScriptOutputType.STATUS, keys, getDeviceType());

        }

        connection.async().hset(key, hashKey, getDeviceType());
        return null;

    }

    /**
     * 获取当前LanternContext中Request的设备类型
     *
     * @return
     */
    private String getDeviceType() {
        String UA = LanternContext.getContext().getRequest().getHeader("User-Agent");

        if (UA.contains("Mobi")) {
            return DeviceType.MOBILE.getName();
        } else if (UA.contains("Windows NT") || UA.contains("Linux")) {
            return DeviceType.BROWSER.getName();
        } else {
            return DeviceType.BROWSER.getName();//TODO:add other device type
        }
    }

    /**
     * 获取当前LanternContext中Request的HashKey(IP+UA)
     *
     * @return
     */

    private String getHashKey() {
        HttpServletRequest request = LanternContext.getContext().getRequest();
        String ip = request.getHeader("x-forwarded-for");
        final String IP = Objects.isNull(ip) ? request.getRemoteAddr() : ip;
        final String UA = request.getHeader("User-Agent");
        return IP + "-" + UA;
    }

    /**
     * 设备类型
     */
    private enum DeviceType {
        /**
         * 浏览器
         */
        BROWSER("BROWSER"),
        /**
         * 手机(浏览器)
         */
        MOBILE("MOBILE"),
        OTHER("OTHER");

        private String name;

        DeviceType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}


/*
 Redis结构 HashMap
 Exist represents online status
 key(systemName-uiId) -> Hash
    {IP,UA} -> {deviceType}
 */
