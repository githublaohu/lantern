package com.lamp.lantern.plugins.api.mode;


import java.time.LocalDateTime;


import com.lamp.lantern.plugins.api.enums.DeviceEnum;
import com.lamp.lantern.plugins.api.enums.ExitWayEnum;
import com.lamp.lantern.plugins.api.enums.LoginPatternEnum;
import com.lamp.lantern.plugins.api.enums.LoginStatusEnum;
import com.lamp.lantern.plugins.api.enums.SystemEnum;
import com.lamp.lantern.plugins.api.enums.TerminalEnum;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
@AllArgsConstructor
public class LoginRecord extends OperateInfoInjection {

	/**
     * 登录记录唯一Id
     */
    private long ulId;

    /**
     * 用户唯一Id
     */
    private long uiId;

    /**
     * 用户登录时间
     */
    private LocalDateTime ulLoginTime;

    /**
     * 用户退出方式
     */
    private LocalDateTime ulQuitTime;

    /**
     * 用户登录地址
     */
    private String ulLoginAddress;

    /**
     * 用户登录Id
     */
    private String ulLoginIp;

    /**
     * 用户登录设备
     */
    private DeviceEnum ulLoginDevice;

    /**
     * 用户登录设备型号
     */
    private String ulLoginDeviceModel;

    /**
     * 用户登录系统
     */
    private SystemEnum ulLoginSystem;

    /**
     * 用户登录方式
     */
    private LoginPatternEnum ulLoginWay;

    /**
     * 用户登录终端
     */
    private TerminalEnum ulLoginTerminal;

    /**
     * 用户退出方式
     */
    private ExitWayEnum ulQuitWay;

    /**
     * 用户第三方Id
     */
    private Integer triId;

    /**
     * 用户登录状态
     */
    private LoginStatusEnum ulLoginStatus;

    private String ulSessionId;

    public LoginRecord() {

    }
}
