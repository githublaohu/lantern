package com.lamp.lantern.plugins.api.mode;

import java.time.LocalDate;
import java.util.Date;

import com.lamp.lantern.plugins.api.enums.StatusEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(value="用户登录对象",description="用户登录对象")
public class UserInfo {
    /**
     * 用户唯一Id
     */
    @ApiModelProperty(value = "用户唯一ID")
    private Long uiId;

    /**
     * 用户名
     */
    private String uiName;

    /**
     * 用户昵称
     */
    private String uiNickname;

    /**
     * 用户唯一标识符
     */
    private String uiIdcard;

    /**
     * 用户手机号
     */
    private String uiPhone;

    /**
     * 用户邮箱
     */
    private String uiEmail;

    /**
     * 用户图像
     */
    private String uiHeadPortrait;

    /**
     * 用户字段缺失标志位
     */
    private Integer uiLackFlag;

    /**
     * 用户性别
     */
    private String uiSex;

    /**
     * 用户生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate uiBirth;

    /**
     * 用户地址
     */
    private String uiAddress;

    /**
     * 用户盐
     */
    private String uiSalt;

    /**
     * 用户盐密
     */
    private String uiSaltPassword;


    /**
     * 用户最近登录地址
     */
    private String uiLoginAddress;

    /**
     * 用户最近登录时间
     */
    private LocalDate uiLoginTime;

    /**
     * 用户最近退出时间
     */
    private LocalDate uiExitTime;


    /**
     * 用户第三方记录表Id, 默认从1开始
     */
    private Integer triId;

    /**
     * 用户状态
     */
    private StatusEnum uiStatus;

    /**
     * 用户是否允许登录 （黑白名单功能）
     */
    private StatusEnum allowLogin;

    /**
     * 用户是否为第一次登录
     */

    private boolean uiFirstLogin;

    private Integer uiIsDelete;

    private String token;

    private String uiPassword;

}
