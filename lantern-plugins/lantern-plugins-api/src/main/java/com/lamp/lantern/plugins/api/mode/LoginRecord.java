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
    private Long ulId;

    private Long systemId;

    /**
     * 用户唯一Id
     */
    private Long uiId;

    private Long triId;

    private String productId;

    /**
     * 用户登录时间
     */
    private LocalDateTime ulLoginTime;

    private String ulLoginAddress;

    private String ulLoginIp;

    private String ulLoginDeviceType;

    private String ulLoginDevice;

    private String ulLoginDeviceModel;

    private String ulLoginSystem;

    private String ulLoginSystemModel;

    private String ulSessionId;

    private String ulLoginWay;

    private String ulLoginWayPlatform;

    private String ulLoginTerminal;

    private String ulLoginTerminalModel;

    private String ulQuitWay;

    private String ulQuitIp;

    private LocalDateTime ulQuitTime;

    private String ulQuitAddress;

    private String ulLoginStatus;

    private String ulLoginFailReason;

    public LoginRecord() {

    }

}
