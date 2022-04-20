package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import com.lamp.lantern.service.core.service.UserInfoService;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;


import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService{


    @Autowired(required = true)
    private UserInfoMapper userInfoMapper;

    public UserInfoEntity queryUserInfoByUiId(UserInfoEntity userInfoEntity) {
        return null;
    }

    public Integer updateUserInfoEntityStatus(UserInfoEntity userInfoEntity) {
        return null;
    }

    public Integer updateUserSignOutByUiId(UserInfoEntity userInfoEntity) {
        return null;
    }

    public Integer updateUserInfoByUiId(UserInfoEntity userInfoEntity) {
        return null;
    }

    public Integer updateLoginByUiId(UserInfoEntity userInfoEntity, LoginRecordEntity userLoginRecord) {
        return null;
    }

    public Integer updatePasswordByUiId(UserInfoEntity userInfoEntity) {
        return null;
    }

    public Integer insertUserInfoEntity(UserInfoEntity userInfoEntity) {
        return userInfoMapper.insertUserInfoEntity(userInfoEntity);
    }


    public UserInfoEntity queryUserInfoByPhone(UserInfoEntity userInfoEntity){
        return userInfoMapper.queryUserInfoByUiPhone(userInfoEntity);
    };

    public UserInfoEntity queryUserInfoByUiPhone(UserInfoEntity userInfoEntity){
        return userInfoMapper.queryUserInfoByUiPhone(userInfoEntity);
    }

    public UserInfoEntity queryUserInfoByLogin(UserInfoEntity userInfoEntity){
        return userInfoMapper.queryUserInfoByLogin(userInfoEntity);

    }

    /**
     * 更新用户信息
     *
     * @param userInfoEntity
     * @return
     */
    public Integer updateUserInfo(UserInfoEntity userInfoEntity){
        return userInfoMapper.updateUserInfo(userInfoEntity);
    }

    public List<String> queryLoginedIpByUiID(UserInfoEntity userInfoEntity){
        return userInfoMapper.queryLoginedIpByUiID(userInfoEntity);
    }

}
