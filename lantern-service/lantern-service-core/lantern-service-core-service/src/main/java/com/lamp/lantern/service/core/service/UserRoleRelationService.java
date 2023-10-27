package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.UserRoleRelationEntity;

import java.util.List;

public interface UserRoleRelationService {
    public Integer insertUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity);

    public Integer insertUserRoleRelations(List<UserRoleRelationEntity> userRoleRelationEntities);

    public Integer endUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity);

    public Integer endUserRoleRelations(List<UserRoleRelationEntity> userRoleRelationEntities);

    public Integer updateUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity);

}
