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

import java.util.Date;

import com.lamp.lantern.plugins.api.enums.StatusEnum;

import lombok.Data;

@Data
public class UserInfo {
    /**
     * 用户唯一Id
     */
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
    
    private String token;
}
