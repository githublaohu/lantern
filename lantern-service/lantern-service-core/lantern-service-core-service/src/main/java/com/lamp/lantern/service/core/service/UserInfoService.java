package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;

import java.util.List;

public interface UserInfoService {

    /**
     * 查询单个用户 账户, 手机, 邮箱
     * @param userInfoEntity
     * @return
     */
    public UserInfoEntity queryUserInfoByLogin(UserInfoEntity userInfoEntity);

    /**
     *
     * @param userInfoEntity
     * @return
     */
    public UserInfoEntity queryUserInfoByUiId(UserInfoEntity userInfoEntity);

    /**
     * 通过手机号 查询用户是否存在
     * @param userInfoEntity
     * @return
     */
    public UserInfoEntity queryUserInfoByUiPhone(UserInfoEntity userInfoEntity);


    /**
     * 更新用户状态
     * @param userInfoEntity
     * @return
     */
    public Integer updateUserInfoEntityStatus(UserInfoEntity userInfoEntity);

    /**
     * 用户退出使用
     * @param userInfoEntity
     * @return
     */
    public Integer updateUserSignOutByUiId(UserInfoEntity userInfoEntity);

    /**
     * 更新用户信息
     * @param userInfoEntity
     * @return
     */
    public Integer updateUserInfoByUiId(UserInfoEntity userInfoEntity);

    /**
     * 登录成功后修改登录信息
     * @param userInfoEntity
     * @return
     */
    public Integer updateLoginByUiId(UserInfoEntity userInfoEntity, LoginRecordEntity userLoginRecord);

    /**
     * 更改密码
     * @param userInfoEntity
     * @return
     */
    public Integer updatePasswordByUiId(UserInfoEntity userInfoEntity);

    /**
     * 添加用户</br>
     * 用户自己注册</br>
     * 第三方登录</br>
     * 管理员添加</br>
     * 管理员从第三方添加</br>
     *
     * @param userInfoEntity
     * @return
     */
    public Integer insertUserInfoEntity(UserInfoEntity userInfoEntity);




    /**
     * 查询用户登录过的Ip
     *
     * @param userInfoEntity
     * @return
     */
    public List<String> queryLoginedIpByUiID(UserInfoEntity userInfoEntity);

    /**
     * 通过手机号查询用户
     *
     * @param userInfoEntity
     * @return
     */
    public UserInfoEntity queryUserInfoByPhone(UserInfoEntity userInfoEntity);


    /**
     * 更改整个用户信息
     *
     * @param userInfoEntity
     * @return
     */
    public Integer updateUserInfo(UserInfoEntity userInfoEntity);


};
