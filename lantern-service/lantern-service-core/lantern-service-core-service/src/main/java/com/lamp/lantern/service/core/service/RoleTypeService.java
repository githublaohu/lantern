package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.RoleTypeEntity;

import java.util.List;

public interface RoleTypeService {
    public Integer insertRoleType(RoleTypeEntity roletypeEntity);

    public Integer insertRoleTypes(List<RoleTypeEntity> roletypeEntities);

    public Integer deleteRoleType(RoleTypeEntity roletypeEntity);

    public Integer deleteRoleTypes(List<RoleTypeEntity> roletypeEntities);
}
