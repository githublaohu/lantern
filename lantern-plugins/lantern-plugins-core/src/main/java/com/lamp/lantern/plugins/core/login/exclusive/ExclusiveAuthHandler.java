package com.lamp.lantern.plugins.core.login.exclusive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.LanternContext;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExclusiveAuthHandler extends AbstractAuthHandler<ExclusiveConfig> {
    private static final String VALUE_KEY = "loginTimesAuthHandler";


    @Override
    public ResultObject<String> authBefore(UserInfo userInfo) {
        ExclusiveInfo exclusiveInfo = new ExclusiveInfo();
        LanternContext.getContext().setValue(VALUE_KEY, exclusiveInfo);

        HttpServletRequest request = LanternContext.getContext().getRequest();
        String IP = request.getHeader("x-forwarded-for");
        exclusiveInfo.setIP(Objects.isNull(IP) ? request.getRemoteAddr() : IP);
        exclusiveInfo.setUA(request.getHeader("User-Agent"));
        exclusiveInfo.setDeviceType(getDeviceType(exclusiveInfo.getUA()));


        if (config.getMethod() == ExclusiveConfig.Method.REFUSE) {
            String key = this.systemName + "-" + userInfo.getUiId().toString();
            JSONArray result = JSON.parseArray(connection.sync().get(key));
            //有相同type的设备在线
            if (result.stream().anyMatch(o -> {
                JSONObject jsonObject = (JSONObject) o;
                return jsonObject.getString("deviceType").equals(exclusiveInfo.getDeviceType().getName()) && jsonObject.getString("deviceStatus").equals(DeviceStatus.ONLINE.getName());
            })) {
                return ResultObject.getResultObjectMessgae(10001, "该设备已经登录");
            }
        }
        return null;
    }

    @Override
    public ResultObject<String> authAfter(UserInfo userInfo) {
        //TODO: heartbeat & TTL
        ExclusiveInfo exclusiveInfo = LanternContext.getContext().getValue(VALUE_KEY);
        String key = this.systemName + "-" + userInfo.getUiId().toString();

        AtomicBoolean isNew = new AtomicBoolean(true);
        //获取该用户的所有设备
        JSONArray result = JSON.parseArray(connection.sync().get(key));
        result = Objects.isNull(result) ? new JSONArray() : result;
        result.forEach(o -> {
            //对于每一个设备
            JSONObject jsonObject = (JSONObject) o;
            //如果和当前登录同一种设备
            if (jsonObject.getString("deviceType").equals(exclusiveInfo.getDeviceType().getName())) {
                //如果该设备是当前设备,则更新设备状态
                if (jsonObject.getString("IP").equals(exclusiveInfo.getIP()) && jsonObject.getString("UA").equals(exclusiveInfo.getUA())) {
                    jsonObject.put("deviceStatus", DeviceStatus.ONLINE.getName());
                    isNew.set(false);
                    //允许同一设备多次登录?
                    //TODO: 通知踢出该设备的其他登录
                }
                //不是当前设备,但是在线同种设备,则踢掉
                else if (jsonObject.getString("deviceStatus").equals(DeviceStatus.ONLINE.getName()) && config.getMethod() == ExclusiveConfig.Method.KICK) {
                    jsonObject.put("deviceStatus", DeviceStatus.OFFLINE.getName());
                    //TODO 通知被踢掉的设备
                }
            }
        });
        //如果是新设备,则添加到设备列表
        if (isNew.get()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("IP", exclusiveInfo.getIP());
            jsonObject.put("UA", exclusiveInfo.getUA());
            jsonObject.put("deviceType", exclusiveInfo.getDeviceType().getName());
            jsonObject.put("deviceStatus", DeviceStatus.ONLINE.getName());
            result.add(jsonObject);
        }
        connection.sync().set(key, JSON.toJSONString(result));


        return null;
    }


    @Data
    private class ExclusiveInfo {
        private String IP;
        private String UA;
        private DeviceType deviceType;
        private DeviceStatus deviceStatus;
    }

    private DeviceType getDeviceType(String UA) {
        if (UA.contains("Mobi")) {
            return DeviceType.MOBILE;
        } else if (UA.contains("Windows NT") || UA.contains("Linux")) {
            return DeviceType.BROWSER;
        } else {
            return DeviceType.BROWSER;//TODO:add other device type
        }
    }

    private enum DeviceType {
        BROWSER("BROWSER"),
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

    private enum DeviceStatus {
        ONLINE("ONLINE"),
        OFFLINE("OFFLINE");

        private String name;

        DeviceStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}


/*
 Redis结构 Hash
 key(systemName-uiId) -> JSON
[
    {
      "IP": "1",
      "UA": "1",
      "deviceType": "1",
      "deviceStatus": "1"
    }
    ...
  ]
 */
