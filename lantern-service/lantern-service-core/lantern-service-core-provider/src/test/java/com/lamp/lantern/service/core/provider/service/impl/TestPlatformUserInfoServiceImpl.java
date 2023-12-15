package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.service.core.entity.PlatformUserInfoEntity;
import com.lamp.lantern.service.core.provider.LanternServiceCoreProviderApplication;
import com.lamp.lantern.service.core.service.PlatformUserInfoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LanternServiceCoreProviderApplication.class)
public class TestPlatformUserInfoServiceImpl {
    @Autowired
    private PlatformUserInfoService platformUserInfoService;

    private final PlatformUserInfoEntity platformUserInfoEntity = new PlatformUserInfoEntity();

    @Before
    public void setEntity(){
        platformUserInfoEntity.setSystemId(1L);
        platformUserInfoEntity.setUserId(1L);
        platformUserInfoEntity.setCorporationId("coId");
        platformUserInfoEntity.setAppId("appId");
        platformUserInfoEntity.setPuiOpenId("openId");
        platformUserInfoEntity.setPuiUnionId("unionId");
        platformUserInfoEntity.setPuiType(LoginType.THIRD.getType());
        platformUserInfoEntity.setPuiAuthChannel("Channel");
        platformUserInfoEntity.setPuiStatus(0);
        platformUserInfoEntity.setOperatorId(1L);
    }

    @Test
    public void testRegister(){
        Integer i = platformUserInfoService.registerPlatformUserInfo(platformUserInfoEntity);
        Assert.assertNotNull(i);
    }

}
