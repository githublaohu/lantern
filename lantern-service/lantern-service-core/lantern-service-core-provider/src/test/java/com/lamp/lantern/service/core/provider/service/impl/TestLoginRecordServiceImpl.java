package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.enums.LoginPatternEnum;
import com.lamp.lantern.plugins.api.enums.LoginStatusEnum;
import com.lamp.lantern.plugins.api.enums.SystemEnum;
import com.lamp.lantern.plugins.api.enums.TerminalEnum;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.LanternServiceCoreProviderApplication;
import com.lamp.lantern.service.core.service.LoginRecordService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LanternServiceCoreProviderApplication.class)
public class TestLoginRecordServiceImpl {

    @Autowired
    private LoginRecordService loginRecordService;
    private final LoginRecordEntity loginRecordEntity = new LoginRecordEntity();

    @Before
    public void setEntity() {
        loginRecordEntity.setUiId(1L);
        loginRecordEntity.setSystemId(1L);
        loginRecordEntity.setUlSessionId("123");
        loginRecordEntity.setUlLoginTime(LocalDateTime.now());
        loginRecordEntity.setUlLoginAddress("Changsha");
        loginRecordEntity.setUlLoginIp("211.1.1.1");
        loginRecordEntity.setUlLoginSystem(SystemEnum.IOS.getSystem());
        loginRecordEntity.setUlLoginWay(LoginPatternEnum.PARTY_ACCOUNT.getParty());
        loginRecordEntity.setUlLoginTerminal(TerminalEnum.MbBrowser.getTerminal());
        loginRecordEntity.setTriId(1L);
        loginRecordEntity.setUlLoginStatus(LoginStatusEnum.SUCCESS.getStatus());
    }

    @Test
    public void testInsert() {
        loginRecordService.insertLoginRecord(loginRecordEntity);
        loginRecordService.insertLoginRecord(loginRecordEntity);
    }

    @Test
    public void testCheck() {
        loginRecordService.insertLoginRecord(loginRecordEntity);
        loginRecordService.insertLoginRecord(loginRecordEntity);
        List<LoginRecordEntity> loginRecordEntities = loginRecordService.checkLoginRecordByUserId(new UserInfoEntity(1L));
        Assert.assertEquals("211.1.1.1", loginRecordEntities.get(0).getUlLoginIp());
    }
}
