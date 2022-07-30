package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import com.lamp.lantern.service.core.service.UserInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired(required = true)
    private UserInfoMapper userInfoEntityMapper;


    @Override
    public Integer registerUserInfoEntity(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.registerUserInfoEntity(userInfoEntity);
    }

    @Override
    public UserInfoEntity checkUserExistByUserName(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByUserName(userInfoEntity);
    }

    @Override
    public UserInfoEntity checkUserExistByEmail(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByEmail(userInfoEntity);
    }

    @Override
    public UserInfoEntity checkUserExistByPhone(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByPhone(userInfoEntity);
    }

    @Override
    public UserInfoEntity queryUserByUserName(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.queryUserByUserName(userInfoEntity);
    }

    @Override
    public UserInfoEntity testQuery() {
        return userInfoEntityMapper.testQuery();
    }

    @Override
    public Integer quertUserById() {
        return userInfoEntityMapper.quertUserById();
    }

    @Override
    public UserInfoEntity checkUserByUserId(UserInfoEntity userInfoEntity) {
        return userInfoEntityMapper.checkUserByUserId(userInfoEntity);
    }

    @Override
    public UserInfoEntity checkUserByUserIdOrPhoneOrEmail(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserByUserIdOrPhoneOrEmail(userInfoEntity);
    }

    @Override
    public Integer updateUserAllowLoginField(UserInfoEntity userInfoEntity) {
        return userInfoEntityMapper.updateUserAllowLoginField(userInfoEntity);
    }

    @Override
    public UserInfoEntity checkUserExistByIdcard(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByIdcard(userInfoEntity);
    }
}
