package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.LanternServiceCoreProviderApplication;
import com.lamp.lantern.service.core.service.RoleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LanternServiceCoreProviderApplication.class)
public class TestRoleServiceImpl {
    @Autowired
    RoleService roleService;

    private RoleEntity roleEntity = new RoleEntity();

    @Before
    public void before(){
        roleEntity.setRoleName("testRoleName");
        roleEntity.setRoleType("testRoleType");
        roleEntity.setRoleTypeId(1L);
        roleEntity.setRoleDescription("testDescription");
        roleEntity.setOperatorId(1L);
        roleEntity.setValidTime(LocalDateTime.now().plusHours(24));
        roleEntity.setStartTime(LocalDateTime.now().minusHours(12));
        roleService.insertRole(roleEntity);
    }

    @Test
    public void testInsert(){
        roleService.insertRole(roleEntity);
    }

    @Test
    public void testValid() throws InterruptedException {
        roleService.insertRole(roleEntity);
        roleEntity = new RoleEntity();
        roleEntity.setRoleName("testRoleName");
        // get last
        List<RoleEntity> roleEntities = roleService.queryByForm(roleEntity);
        roleEntity = roleEntities.get(roleEntities.size()-1);

        roleEntity.setOperatorId(1L);
        Assert.assertEquals(1,roleService.checkRoleValid(roleEntity).intValue());
        Thread.sleep(1000);
        roleService.endRole(roleEntity);
        Assert.assertEquals(0,roleService.checkRoleValid(roleEntity).intValue());
    }

    @Test
    public void updateTest(){
        roleService.insertRole(roleEntity);
        roleEntity.setRoleDescription("testDescriptionUpdate");
        roleEntity.setOperatorId(2L);
        roleService.updateRole(roleEntity);

        roleEntity = roleService.queryByRoleId(roleEntity);
        Assert.assertEquals("testDescriptionUpdate",roleEntity.getRoleDescription());
        Assert.assertEquals(2L,roleEntity.getUpdateUserId().longValue());
    }

    @Test
    public void getValidRolesTest(){
        roleService.insertRole(roleEntity);
        List<RoleEntity> roleEntities = roleService.getValidRoles();
        Assert.assertNotNull(roleEntities);
        Assert.assertEquals(1,roleService.checkRoleValid(roleEntities.get(0)).intValue());
    }

//    @Test
//    public void testByUserId(){
//        UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUiId(11325L);
//
//        List<RoleEntity> roleEntities = roleService.getAllRoleByUserId(userInfoEntity);
//        Assert.assertNotNull(roleEntities);
//        Assert.assertEquals(1,roleEntities.size());
//    }





//    @Test
//    public List<RoleEntity> c = roleService.getValidRoles();
//
//    UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUiId(1L);
//    List<RoleEntity> d = this.getAllValidRoleByUserId(userInfoEntity);
}
