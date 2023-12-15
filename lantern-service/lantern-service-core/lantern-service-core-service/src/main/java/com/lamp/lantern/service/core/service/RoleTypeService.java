package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.RoleTypeEntity;

import java.util.List;

public interface RoleTypeService {
    public Integer insertRoleType(RoleTypeEntity roleTypeEntity);

    public Integer deleteRoleType(RoleTypeEntity roleTypeEntity);

    public Integer deleteRoleTypes(List<RoleTypeEntity> roleTypeEntities);
}
