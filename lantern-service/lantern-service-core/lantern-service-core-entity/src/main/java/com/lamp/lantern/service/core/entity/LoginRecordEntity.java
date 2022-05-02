package com.lamp.lantern.service.core.entity;


import java.io.Serializable;
import java.util.Date;

import com.lamp.lantern.service.core.entity.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;


@Data
@Component
@EqualsAndHashCode
@AllArgsConstructor
@ApiModel(value = "UserInfoEntity", description = "用户信息实体")
public class LoginRecordEntity implements Serializable {

    public LoginRecordEntity(){

    }

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date ulLoginTime;

    /**
     * 用户退出方式
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date ulExitTime;

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


}
