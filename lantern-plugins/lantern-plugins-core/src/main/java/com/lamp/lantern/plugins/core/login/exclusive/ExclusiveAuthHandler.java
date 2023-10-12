package com.lamp.lantern.plugins.core.login.exclusive;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.LanternContext;
import io.lettuce.core.api.StatefulRedisConnection;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 此Handler应该处于登录Handle最后
 * 使用方法见枚举//
 */
public class ExclusiveAuthHandler extends AbstractAuthHandler<ExclusiveConfig> {

    private ResultObject<String> loggedResult;
    private ResultObject<String> limitResult;

    @Override
    public void init() {
        loggedResult = ResultObject.getResultObjectMessgae(10001, "该设备已经登录");
        limitResult = ResultObject.getResultObjectMessgae(10101, "登录设备已达上限");
    }


    @Override
    public ResultObject<String> authBefore(UserInfo userInfo) {

        if (!config.getExclusiveMethod().isRefuseFamily()) {
            return null;
        }
        String key = SystemName + "-" + getHandlerName() + "-" + userInfo.getUiId().toString();

        //REFUSE模式下,如果该设备已经登录,则拒绝登录
        if (config.getExclusiveMethod() == ExclusiveConfig.ExclusiveMethod.REFUSE) {
            if (connection.sync().exists(key) != 0) {
                return loggedResult;
            }
        }
        //ALLOW_NUMBER模式下,检查已经登录设备的数量
        long count = connection.sync().hlen(key);
        if (count >= config.getExclusiveMethod().Number()) {
            return limitResult;
        }
        return null;
    }

    @Override
    public ResultObject<String> authAfter(UserInfo userInfo) {

        String key = SystemName + "-" + getHandlerName() + "-" + userInfo.getUiId().toString();

        //KICK_FAMILY方法
        //把JSON字段修改为KickOut
        if (config.getExclusiveMethod() == ExclusiveConfig.ExclusiveMethod.KICK_ALL) {
            //踢出所有在线设备
            kickAll(userInfo);
            connection.async().del(key);

        } else if (config.getExclusiveMethod() == ExclusiveConfig.ExclusiveMethod.KICK_SAME) {
            //踢出同一设备
            String sameDevice = connection.sync().hget(key, getIP() + "-" + getUserAgent());
            if (Objects.nonNull(sameDevice)) {
                String token = JSON.parseObject(sameDevice).getString("Token");
                if (Objects.nonNull(token)) {
                    kickToken(userInfo, token);
                }
            }
            connection.async().hdel(key, getIP() + "-" + getUserAgent());

        } else if (config.getExclusiveMethod() == ExclusiveConfig.ExclusiveMethod.KICK_TYPE) {
            //踢出同类型设备
            String sameType = connection.sync().hget(key, getDeviceType());
            if (Objects.nonNull(sameType)) {
                String token = JSON.parseObject(sameType).getString("Token");
                kickToken(userInfo, token);
                connection.async().hdel(key, getDeviceType());
            }
        }

        //无论如何登录, 都要将当前设备的信息写入Redis
        //不同的类型, 使用的HashKey不同
        JSONObject data = new JSONObject();
        data.put("User-Agent", getUserAgent());
        data.put("IP", getIP());
        LanternContext context = LanternContext.getContext();
        data.put("Token", LanternContext.getContext().getToken());
        if (config.getExclusiveMethod() == ExclusiveConfig.ExclusiveMethod.KICK_SAME) {
            connection.async().hset(key, getIP() + "-" + getUserAgent(), data.toJSONString());
        }
        if (config.getExclusiveMethod() == ExclusiveConfig.ExclusiveMethod.KICK_TYPE) {
            connection.async().hset(key, getDeviceType(), data.toJSONString());
        }
        connection.async().hset(key, getIP(), data.toJSONString());
        return null;
    }

    /**
     * 获取当前LanternContext中Request的设备类型
     *
     * @return
     */
    private String getDeviceType() {
        String userAgent = LanternContext.getContext().getRequest().getHeader("User-Agent");

        if (userAgent.contains("Mobi")) {
            return DeviceType.MOBILE.getName();
        } else if (userAgent.contains("Windows NT") || userAgent.contains("Linux")) {
            return DeviceType.BROWSER.getName();
        } else {
            return DeviceType.BROWSER.getName();//TODO:add other device type
        }
    }


    private String getUserAgent() {
        return LanternContext.getContext().getRequest().getHeader("User-Agent");
    }

    private String getIP() {
        HttpServletRequest request = LanternContext.getContext().getRequest();
        String ip = request.getHeader("x-forwarded-for");
        return Objects.isNull(ip) ? request.getRemoteAddr() : ip;
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

    /**
     * 这个方法使用了TokenHandler的Redis连接
     */

    private void kickAll(UserInfo userInfo) {
        StatefulRedisConnection connection = LanternContext.getContext().getSessionWorkInfo().getConnection();
        connection.sync().keys(LanternContext.getContext().getSessionWorkInfo().getTokenHandlerName() + "-" + userInfo.getUiId().toString() + ":*").forEach(key -> {
            JSONObject current = JSON.parseObject(connection.sync().get(key).toString());
            current.put("Status", "KickOut");
            connection.async().set(key, current.toJSONString());
        });
    }

    private void kickToken(UserInfo userInfo, String token) {
        StatefulRedisConnection connection = LanternContext.getContext().getSessionWorkInfo().getConnection();
        Object current = connection.sync().get(LanternContext.getContext().getSessionWorkInfo().getTokenHandlerName() + "-" + userInfo.getUiId().toString() + ":" + token);
        if (Objects.isNull(current)) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(current.toString());
        jsonObject.put("Status", "KickOut");
        connection.async().set(LanternContext.getContext().getSessionWorkInfo().getTokenHandlerName() + "-" + userInfo.getUiId().toString() + ":" + token, jsonObject.toJSONString());
    }


}


/*
 Redis结构 HashMap
 Exist represents online status
 key(systemName-handlerName-uiId) -> Hash
 Different Method uses different HashKey
    REFUSE: IP -> Data
    ALLOW_N: IP -> Data
    KICK_ALL: IP -> Data
    KICK_SAME: {IP-UA} -> Data
    KICK_TYPE: Type -> Data
 Data: JSON:{"Token","User-Agent","IP"}

 */
