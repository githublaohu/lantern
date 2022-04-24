package com.lamp.lantern.serivce.action.login.utils;

import com.google.common.io.Resources;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;


public class IpUtils {

    public static String getIpAddr(HttpServletRequest request){
        return "";
    }

    public static String getCityInfo(HttpServletRequest request){
        String ipAddr = IpUtils.getIpAddr(request);
        return "中国|华南｜广东省|深圳市";
    }
}
