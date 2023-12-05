package com.lamp.lantern.service.core.service;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface UserInfoService {

    public UserInfoEntity registerUserInfoEntity(UserInfoEntity userInfoEntity);

    public UserInfo checkUserExistByUserName(UserInfoEntity userInfoEntity);

    public UserInfo checkUserExistByEmail(UserInfoEntity userInfoEntity);

    public UserInfo checkUserExistByPhone(UserInfoEntity userInfoEntity);

    public UserInfo queryUserByUserName(UserInfoEntity userInfoEntity);

    public UserInfo checkUserByUserId(UserInfoEntity userInfoEntity);


    public UserInfo checkUserByUserIdOrPhoneOrEmail(UserInfoEntity userInfoEntity);

    public UserInfo checkUserExistByIdcard(UserInfoEntity userInfoEntity);

    public Integer updateUserAllowLoginField(UserInfoEntity userInfoEntity);

    public UserInfo testQuery();

    public UserInfo queryUserById(UserInfoEntity userInfoEntity);

    public Integer deleteUser(UserInfoEntity userInfoEntity);
    public Integer deleteUsers(List<UserInfoEntity> userInfoEntities);

    public List<UserInfo> getAllUserInfos();

    public List<UserInfo> getUpdatedUserInfos(LocalDateTime time);

    public UserInfo registerThirdLoginUser(UserInfoEntity userInfo);
}
