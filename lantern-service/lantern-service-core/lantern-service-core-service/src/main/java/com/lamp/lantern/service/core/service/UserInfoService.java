package com.lamp.lantern.service.core.service;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface UserInfoService {

    public UserInfoEntity registerUserInfoEntity(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByUserName(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByEmail(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByPhone(UserInfoEntity userInfoEntity);

    public UserInfoEntity queryUserByUserName(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserByUserId(UserInfoEntity userInfoEntity);


    public UserInfoEntity checkUserByUserIdOrPhoneOrEmail(UserInfoEntity userInfoEntity);

    public UserInfoEntity checkUserExistByIdcard(UserInfoEntity userInfoEntity);

    public Integer updateUserAllowLoginField(UserInfoEntity userInfoEntity);

    public UserInfoEntity testQuery();

    public UserInfoEntity queryUserById(UserInfoEntity userInfoEntity);

    public Integer deleteUser(UserInfoEntity userInfoEntity);
    public Integer deleteUsers(List<UserInfoEntity> userInfoEntities);

    public List<UserInfoEntity> getAllUserInfos();

    public List<UserInfoEntity> getUpdatedUserInfos(LocalDateTime time);

    public UserInfoEntity registerThirdLoginUser(UserInfoEntity userInfo);
}
