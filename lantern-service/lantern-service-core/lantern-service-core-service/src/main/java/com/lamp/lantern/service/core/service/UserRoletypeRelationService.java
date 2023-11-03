package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.UserRoletypeRelationEntity;

import java.util.List;

public interface UserRoletypeRelationService {
    public Integer insertUserRoletypeRelation(UserRoletypeRelationEntity userRoletypeRelationEntity);

    public Integer insertUserRoletypeRelations(List<UserRoletypeRelationEntity> userRoletypeRelationEntities);

    public Integer endUserRoletpeRelation(UserRoletypeRelationEntity userRoletypeRelationEntity);

    public Integer endUserRoletpeRelations(List<UserRoletypeRelationEntity> userRoletypeRelationEntities);

    public Integer updateUserRoletypeRelation(UserRoletypeRelationEntity userRoletypeRelationEntity);
}
