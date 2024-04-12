package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.enums.StatusEnum;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.LanternServiceCoreProviderApplication;
import com.lamp.lantern.service.core.service.UserInfoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LanternServiceCoreProviderApplication.class)
public class TestUserInfoServiceImpl {
    @Autowired
    private UserInfoService userInfoService;

    private UserInfoEntity userInfoEntity = new UserInfoEntity();

    @Before
    public void setEntity() {
        userInfoEntity.setUiName("admin");
        userInfoEntity.setUiIdCard("123456789");
        userInfoEntity.setUiPhone("123456789");
        userInfoEntity.setUiEmail("123456789");
        userInfoEntity.setUiHeadPortrait("123456789");
        userInfoEntity.setUiAddress("123456789");
        userInfoEntity.setUiSalt("123456");
        userInfoEntity.setUiNickname("admin");
        userInfoEntity.setUiSex("MALE");
        userInfoEntity.setUiBirth(LocalDate.now());
        userInfoEntity.setUiSaltPassword("123456");
        userInfoEntity.setUiStatus(1L);
        userInfoEntity.setSystemId(1L);
        userInfoEntity.setUiAllowLogin(StatusEnum.ACTIVE);
        userInfoEntity.setUiEmail("23123123");
        userInfoEntity.setUiAddress("123123");
        userInfoEntity.setOperatorId(1L);
        userInfoEntity.setUiPhone("123123");
        userInfoEntity.setUiLoginTime(LocalDateTime.now());
        userInfoEntity.setUiExitTime(LocalDateTime.now());
        userInfoEntity.setUiLoginAddress("123123");
    }

    @Test
    public void registerUserInfoEntity() {
        userInfoService.registerUserInfoEntity(userInfoEntity);
    }


    @After
    public void deleteAllUserInfoEntity() {
        List<UserInfoEntity> userInfoEntities = userInfoService.getAllUserInfos();
        userInfoService.deleteUsers(userInfoEntities);
    }
}

//TODO
//RRR
//R
//        RT
//UI

