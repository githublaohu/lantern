package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface UserInfoService {

    public Integer registerUserInfoEntity(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByUserName(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByEmail(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByPhone(UserInfoEntity userInfoEntity);

    public UserInfoEntity queryUserByUserName(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserByUserId(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserByUserIdOrPhoneOrEmail(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByIdcard(UserInfoEntity userInfoEntity);

    public Integer updateUserAllowLoginField(UserInfoEntity userInfoEntity);

    public UserInfoEntity testQuery();

    public Integer quertUserById();

}
