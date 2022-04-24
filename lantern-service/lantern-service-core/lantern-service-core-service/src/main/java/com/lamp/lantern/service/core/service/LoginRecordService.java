package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;

import java.util.List;

public interface LoginRecordService {

    /**
     * 查询用户所有登录记录
     */
    public List<LoginRecordEntity> queryLoginRecordByUiId(UserInfoEntity userInfoEntity);

    /**
     * 插入新增的登录记录
     */
    public Integer insertLoginRecord(LoginRecordEntity userLoginRecord);


    /**
     * 修改登录记录状态 既退出登录后更改用户退出时间、退出方式
     */
    public Integer updateLoginRecord(LoginRecordEntity userLoginRecord);


    /**
     * 查询用户当天失败登录次数
     */
    public Integer queryUserValidLoginRecordToday(UserInfoEntity userInfoEntity);
}
