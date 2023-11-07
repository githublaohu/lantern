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

//    @PostConstruct
//    void test() {
//        UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUiName("admin");
//        userInfoEntity.setUiIdcard("123456789");
//        userInfoEntity.setUiSalt("123456");
//        userInfoEntity.setUiNickname("admin");
//        userInfoEntity.setUiBirth(LocalDate.now());
//        userInfoEntity.setUiSaltPassword("123456");
//        userInfoEntity.setUiStatus(StatusEnum.ACTIVE);
//        userInfoEntity.setAllowLogin(StatusEnum.ACTIVE);
//        userInfoEntity.setUiEmail("23123123");
//        userInfoEntity.setUiAddress("123123");
//        userInfoEntity.setUiPhone("123123");
//        userInfoEntity.setUiLoginAddress("123123");
//        userInfoEntity.setUiLackFlag(0);
//        userInfoEntity.setUiFirstLogin(true);
//        String a = userInfoEntity.getUiPhone();
//        boolean b = userInfoEntity.getUiPhone() == null;
//        this.registerUserInfoEntity(userInfoEntity);
//    }


    @Override
    public UserInfoEntity registerUserInfoEntity(UserInfoEntity userInfoEntity){
        userInfoEntityMapper.registerUserInfoEntity(userInfoEntity);
        return userInfoEntity;
    }

    @Override
    public UserInfo checkUserExistByUserName(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByUserName(userInfoEntity);
    }

    @Override
    public UserInfo checkUserExistByEmail(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByEmail(userInfoEntity);
    }

    @Override
    public UserInfo checkUserExistByPhone(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByPhone(userInfoEntity);
    }

    @Override
    public UserInfo queryUserByUserName(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.queryUserByUserName(userInfoEntity);
    }

    @Override
    public UserInfo testQuery() {
        return userInfoEntityMapper.testQuery();
    }

    @Override
    public Integer queryUserById() {
        return userInfoEntityMapper.quertUserById();
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
    public List<UserInfo> getAllUserInfos() {
        return userInfoEntityMapper.getAllUserInfos();
    }

    @Override
    public List<UserInfo> getUpdatedUserInfos(LocalDateTime time){
        return userInfoEntityMapper.getUpdatedUserInfos(time);
    }

    @Override
    public UserInfo registerThirdLoginUser(UserInfoEntity userInfo) {
        userInfoEntityMapper.registerThirdLoginUser(userInfo);
        return userInfo;
    }


    @Override
    public UserInfo checkUserByUserId(UserInfoEntity userInfoEntity) {
        return userInfoEntityMapper.checkUserByUserId(userInfoEntity);
    }


    @Override
    public UserInfo checkUserByUserIdOrPhoneOrEmail(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserByUserIdOrPhoneOrEmail(userInfoEntity);
    }

    @Override
    public Integer updateUserAllowLoginField(UserInfoEntity userInfoEntity) {
        return userInfoEntityMapper.updateUserAllowLoginField(userInfoEntity);
    }

    @Override
    public UserInfo checkUserExistByIdcard(UserInfoEntity userInfoEntity){
        return userInfoEntityMapper.checkUserExistByIdcard(userInfoEntity);
    }
}
