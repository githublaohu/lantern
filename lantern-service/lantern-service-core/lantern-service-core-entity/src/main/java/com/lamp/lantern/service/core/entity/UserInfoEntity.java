package com.lamp.lantern.service.core.entity;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import io.swagger.annotations.ApiModel;

import com.lamp.lantern.service.core.entity.enums.GenderEnum;
import com.lamp.lantern.service.core.entity.enums.BooleanEnum;
import com.lamp.lantern.service.core.entity.enums.StatusEnum;



@Data
@EqualsAndHashCode
@AllArgsConstructor
@ApiModel(value = "UserInfoEntity", description = "用户信息实体")
public class UserInfoEntity implements Serializable {

    public UserInfoEntity(){

    }
    /**
     * 用户唯一Id
     */
    private int uiId;

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
    private GenderEnum uiSex;

    /**
     * 用户年龄
     */
    private Integer uiAge;

    /**
     * 用户生日
     */
    private Date uiBirth;

    /**
     * 用户地址
     */
    private String uiAddress;

    /**
     * 用户密码 需要加密
     */
    private String uiPassword;

    /**
     * 用户盐
     */
    private String uiSalt;

    /**
     * 用户盐密
     */
    private String uiSaltPassword;

    /**
     * 用户令牌
     */
    private String uiToken;

    /**
     * 用户最近登录地址
     */
    private String uiLoginAddress;

    /**
     * 用户最近登录时间
     */
    private Date uiLoginTime;

    /**
     * 用户最近退出时间
     */
    private Date uiExitTime;

    /**
     * 用户是否为第一次登录
     */
    private BooleanEnum uiFirstLogin;

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


}
