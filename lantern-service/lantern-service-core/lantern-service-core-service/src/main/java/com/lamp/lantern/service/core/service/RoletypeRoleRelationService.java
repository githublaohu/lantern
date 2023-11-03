package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.RoletypeRoleRelationEntity;

import java.util.List;

public interface RoletypeRoleRelationService {
    public Integer insertRoletypeRoleRelation(RoletypeRoleRelationEntity roletypeRoleRelationEntity);

    public Integer insertRoletypeRoleRelations(List<RoletypeRoleRelationEntity> roletypeRoleRelationEntities);

    public Integer endRoletpeRoleRelation(RoletypeRoleRelationEntity roletypeRoleRelationEntity);
    public Integer endRoletpeRoleRelations(List<RoletypeRoleRelationEntity> roletypeRoleRelationEntities);
    public Integer updateRoletypeRoleRelation(RoletypeRoleRelationEntity roletypeRoleRelationEntity);
}
