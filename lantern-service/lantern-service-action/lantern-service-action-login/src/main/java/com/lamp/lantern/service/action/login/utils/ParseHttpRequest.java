package com.lamp.lantern.service.action.login.utils;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.service.core.entity.enums.DeviceEnum;
import com.lamp.lantern.service.core.entity.enums.SystemEnum;
import com.lamp.lantern.service.core.entity.enums.TerminalEnum;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import com.lamp.lantern.service.action.login.utils.HttpClientUtil;

public class ParseHttpRequest {

    public static String getRometeIp(HttpServletRequest request){
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    public static String getRemoteAddress(HttpServletRequest request){
        String ip = ParseHttpRequest.getRemoteAddress(request);
        String result = "";
        try {

            ClassPathResource resource = new ClassPathResource("GeoLite2-City.mmdb");
            DatabaseReader reader = new DatabaseReader.Builder(resource.getInputStream()).build();
            InetAddress inetAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(inetAddress);

            String country = response.getCountry().getNames().get("ja");

            if (response.getSubdivisions().size() > 0) {

                String province = response.getSubdivisions().get(0).getNames().get("zh-CN");

                String city = response.getCity().getNames().get("zh-CN");
                city = city == null ? "" : city;
                result = country + province + city;
            } else {
                result = getDetailedAddress(ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getDetailedAddress(String ip) throws IOException {
        Map map = new HashMap();
        map.put("ip", ip);
        map.put("accessKey", "alibaba-inc");
        String result = HttpClientUtil.post("http://ip.taobao.com/outGetIpInfo", map);
        Map valueMap = JSONObject.parseObject(result, Map.class);

        if ("query success".equals(valueMap.get("msg"))) {
            Map<String, String> dataMap = (Map<String, String>) valueMap.get("data");
            String country = dataMap.get("country");
            String region = dataMap.get("region");
            String city = dataMap.get("city");
            return country + region + city;
        }
        return "";
    }

    public static DeviceEnum getDeviceEnum(HttpServletRequest request){
        // TODO

        return DeviceEnum.APPLE;
    }

    public static TerminalEnum getTerminalEnum(HttpServletRequest request){
        // TODO

        return TerminalEnum.MbBrowser;
    }

    public static String getDeviceModel(HttpServletRequest request){
        // TODO

        return "UNKNOWN";
    }

    public static SystemEnum getLoginSystem(HttpServletRequest request){
        // TODO

        return SystemEnum.IOS;
    }

    public static void main(String[] args) throws IOException {
        String detailedAddress = getDetailedAddress("14.127.123.1");
        System.out.println(detailedAddress);


    }
}
