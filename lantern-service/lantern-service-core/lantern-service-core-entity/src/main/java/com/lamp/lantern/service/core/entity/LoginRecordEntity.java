package com.lamp.lantern.service.core.entity;


import com.lamp.lantern.service.core.entity.enums.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@ApiModel(value = "UserLoginRecordEntity", description = "用户登录记录实体")
public class LoginRecordEntity {

    public LoginRecordEntity(){

    }

    /**
     * 用户id
     */
    private int uiId;

    /**
     * 用户登录时间
     */
    private Date ulLoginTime;

    /**
     * 用户退出时间
     */
    private Date ulExitTime;

    /**
     * 用户登录Ip
     */
    private String ulLoginIp;

    /**
     * 用户登录地址
     */
    private String ulLoginAddress;

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
    private OperateSystemEnum ulLoginSystem;

    /**
    * 用户登录方式
    */
    private LoginwayEnum ulLoginWay;


    /**
     * 用户登录终端
     */
    private TerminalEnum ulLoginTerminal;

    /**
     * 用户退出方式
     */
    private ExitModeEnum ulExitWay;

    /**
     * 第三方平台Id
     */
    private int triId;

    /**
     * 用户登录成功状态
     */
    private StatusEnum ulLoginStatus;



}
