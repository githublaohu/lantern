package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoleTypeEntity;
import com.lamp.lantern.service.core.provider.mapper.RoleTypeMapper;
import com.lamp.lantern.service.core.service.RoleTypeService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class RoleTypeServiceImpl implements RoleTypeService {
    @Autowired
    private RoleTypeMapper roletypeMapper;
//    @PostConstruct
//    public void test(){
//        RoleTypeEntity roletypeEntity = new RoleTypeEntity();
//        roletypeEntity.setRoleTypeName("admin");
//        this.insertRoleType(roletypeEntity);
//    }
    @Override
    public Integer insertRoleType(RoleTypeEntity roletypeEntity) {
        return roletypeMapper.insertRoleType(roletypeEntity);
    }

    @Override
    public Integer insertRoleTypes(List<RoleTypeEntity> roletypeEntities) {
        roletypeEntities.forEach(this::insertRoleType);
        return 1;
    }

    @Override
    public Integer deleteRoleType(RoleTypeEntity roletypeEntity) {
        return roletypeMapper.deleteRoleType(roletypeEntity);
    }

    @Override
    public Integer deleteRoleTypes(List<RoleTypeEntity> roletypeEntities) {
        return roletypeMapper.deleteRoleTypes(roletypeEntities);
    }

}
