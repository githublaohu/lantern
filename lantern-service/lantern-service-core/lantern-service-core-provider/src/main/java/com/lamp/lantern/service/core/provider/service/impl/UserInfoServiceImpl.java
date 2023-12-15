package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.enums.StatusEnum;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import com.lamp.lantern.service.core.service.UserInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired(required = true)
    private UserInfoMapper userInfoEntityMapper;


    @Override
    public UserInfoEntity registerUserInfoEntity(UserInfoEntity userInfoEntity){
        userInfoEntityMapper.registerUserInfoEntity(userInfoEntity);
        return userInfoEntity;
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
        return userInfoEntityMapper.checkUserExistByUserName(userInfoEntity);
    }

    @Override
    public UserInfoEntity testQuery() {
        return userInfoEntityMapper.testQuery();
    }

    @Override
    public UserInfoEntity queryUserById(UserInfoEntity userInfoEntity) {
        return userInfoEntityMapper.checkUserByUserId(userInfoEntity);
    }

    @Override
    public Integer deleteUser(UserInfoEntity userInfoEntity) {
        return userInfoEntityMapper.deleteUser(userInfoEntity);
    }

    @Override
    public Integer deleteUsers(List<UserInfoEntity> userInfoEntities) {
        return userInfoEntityMapper.deleteUsers(userInfoEntities);
    }

    @Override
    public List<UserInfoEntity> getAllUserInfos() {
        return userInfoEntityMapper.getAllUserInfos();
    }

    @Override
    public List<UserInfoEntity> getUpdatedUserInfos(LocalDateTime time){
        return userInfoEntityMapper.getUpdatedUserInfos(time);
    }

    @Override
    public UserInfoEntity registerThirdLoginUser(UserInfoEntity userInfo) {
        userInfoEntityMapper.registerThirdLoginUser(userInfo);
        return userInfo;
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
