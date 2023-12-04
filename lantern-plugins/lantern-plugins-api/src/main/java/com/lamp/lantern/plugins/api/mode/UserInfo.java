/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.api.mode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import com.lamp.lantern.plugins.api.enums.StatusEnum;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(value="用户登录对象",description="用户登录对象")
public class UserInfo extends OperateInfoInjection implements Serializable {
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
