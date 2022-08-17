package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.AuthRoleRelationEntity;

import java.util.List;

public interface AuthRoleRelationService {

  public List<AuthRoleRelationEntity> queryAuthRoleRelation (AuthRoleRelationEntity authRoleRelation);

  public Integer insertAuthRoleRelation(AuthRoleRelationEntity authRoleRelation);

  public Integer updateAuthRoleRelation(AuthRoleRelationEntity authRoleRelation);
}
