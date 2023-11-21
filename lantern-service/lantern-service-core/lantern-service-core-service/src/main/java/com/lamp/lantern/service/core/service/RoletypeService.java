package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.RoletypeEntity;

import java.util.List;

public interface RoletypeService {
    public Integer insertRoletype(RoletypeEntity roletypeEntity);

    public Integer insertRoletypes(List<RoletypeEntity> roletypeEntities);

    public Integer deleteRoletype(RoletypeEntity roletypeEntity);

    public Integer deleteRoletypes(List<RoletypeEntity> roletypeEntities);
}
