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

    @Override
    public Integer insertRoleType(RoleTypeEntity roleTypeEntity) {
        roletypeMapper.insertRoleType(roleTypeEntity);
        return roleTypeEntity.getRoleTypeId().intValue();
    }

    @Override
    public Integer deleteRoleType(RoleTypeEntity roleTypeEntity) {
        return roletypeMapper.deleteRoleType(roleTypeEntity);
    }

    @Override
    public Integer deleteRoleTypes(List<RoleTypeEntity> roleTypeEntities) {
        return roletypeMapper.deleteRoleTypes(roleTypeEntities);
    }

}
