package com.lamp.lantern.serivce.action.login.utils;

import com.lamp.lantern.serivce.action.login.LoginPatternEnums;
import com.lamp.lantern.service.core.entity.enums.DeviceEnum;
import com.lamp.lantern.service.core.entity.enums.LoginwayEnum;
import com.lamp.lantern.service.core.entity.enums.TerminalEnum;
import com.lamp.lantern.service.core.entity.enums.OperateSystemEnum;

import javax.servlet.http.HttpServletRequest;

public class LoginUtils {

    public static DeviceEnum getDeviceType(HttpServletRequest request){
        return DeviceEnum.APPLE;
    }

    public static TerminalEnum getTerminalType(HttpServletRequest request){
        return TerminalEnum.MB_APP;
    }

    public static OperateSystemEnum getOperateSystemType(HttpServletRequest request){
        return OperateSystemEnum.MACOS;
    }

    public static String getDeviceModel(HttpServletRequest request){
        return "";
    }

    public static LoginwayEnum getLoginWayType(LoginPatternEnums loginPatternEnums){
        return LoginwayEnum.ACCOUNT_PASSWORD;
    }
}
