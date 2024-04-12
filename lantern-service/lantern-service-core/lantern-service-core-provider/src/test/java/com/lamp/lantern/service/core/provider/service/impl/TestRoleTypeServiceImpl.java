package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoleTypeEntity;
import com.lamp.lantern.service.core.provider.LanternServiceCoreProviderApplication;
import com.lamp.lantern.service.core.service.RoleTypeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LanternServiceCoreProviderApplication.class)
public class TestRoleTypeServiceImpl {
    @Autowired
    RoleTypeService roleTypeService;

    private ArrayList<Integer> index = new ArrayList<>();

    @Before
    public void insertTest() {
        RoleTypeEntity roletypeEntity = new RoleTypeEntity();
        roletypeEntity.setRoleTypeName("normal");
        roletypeEntity.setRoleTypeDescription("describe");
        Integer i = roleTypeService.insertRoleType(roletypeEntity);
        Assert.assertNotNull(i);
        index.add(i);
    }

    @Test
    public void deleteTest() {
        RoleTypeEntity roletypeEntity = new RoleTypeEntity();
        roletypeEntity.setRoleTypeName("normal");
        roletypeEntity.setRoleTypeDescription("describe");
        Integer i = roleTypeService.insertRoleType(roletypeEntity);

        RoleTypeEntity role2delete = new RoleTypeEntity();
        role2delete.setRoleTypeId(i.longValue());
        Integer d = roleTypeService.deleteRoleType(role2delete);
        Assert.assertNotNull(d);
    }

    @After
    public void deleteListTest() {
        List<RoleTypeEntity> roleTypeEntities = new ArrayList<>();
        for (Integer id : index) {
            RoleTypeEntity roleTypeEntity = new RoleTypeEntity();
            roleTypeEntity.setRoleTypeId(id.longValue());
            roleTypeEntities.add(roleTypeEntity);
        }

        Assert.assertNotNull(roleTypeService.deleteRoleTypes(roleTypeEntities));

    }

}