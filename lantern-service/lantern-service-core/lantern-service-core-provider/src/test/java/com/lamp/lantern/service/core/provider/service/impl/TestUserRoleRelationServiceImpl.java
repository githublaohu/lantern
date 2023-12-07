package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.UserRoleRelationEntity;
import com.lamp.lantern.service.core.provider.LanternServiceCoreProviderApplication;
import com.lamp.lantern.service.core.service.UserRoleRelationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LanternServiceCoreProviderApplication.class)
public class TestUserRoleRelationServiceImpl {

    @Autowired
    private UserRoleRelationService userRoleRelationService;

    private final UserRoleRelationEntity current = new UserRoleRelationEntity();
    private final UserRoleRelationEntity userRoleRelationEntity  = new UserRoleRelationEntity();

    @Before
    public void insert(){
        userRoleRelationEntity.setUserId(1L);
        userRoleRelationEntity.setRoleId(1L);
        userRoleRelationEntity.setOperatorId(1L);
        userRoleRelationEntity.setUrrType("type");
        userRoleRelationEntity.setStartTime(LocalDateTime.now().plusHours(12));
        userRoleRelationEntity.setValidTime(LocalDateTime.now().plusHours(24));
    }
    @Test
    public void endUserRole() throws InterruptedException {
        current.setUrrId(userRoleRelationService.insertUserRoleRelation(userRoleRelationEntity).longValue());
        Assert.assertNotNull(current.getUrrId());
        Thread.sleep(1000);

        Assert.assertNotNull(userRoleRelationService.endUserRoleRelation(current));
    }

    @Test
    public void deleteUserRole(){
        current.setUrrId(userRoleRelationService.insertUserRoleRelation(userRoleRelationEntity).longValue());
        Assert.assertNotNull(current.getUrrId());

        Assert.assertNotNull(userRoleRelationService.deleteUserRoleRelation(current));
    }
}
