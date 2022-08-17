package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.AuthRoleRelationEntity;
import com.lamp.lantern.service.core.provider.Mapper.AuthRoleRelationMapper;
import com.lamp.lantern.service.core.service.AuthRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthRoleRelationServiceImpl implements AuthRoleRelationService {

  @Autowired
  private AuthRoleRelationMapper authRoleRelationMapper;

  @Override
  public List<AuthRoleRelationEntity> queryAuthRoleRelation(AuthRoleRelationEntity authRoleRelation) {
    return authRoleRelationMapper.queryRelation(authRoleRelation);
  }

  @Override
  public Integer insertAuthRoleRelation(AuthRoleRelationEntity authRoleRelation) {
    return authRoleRelationMapper.insertRelation(authRoleRelation);
  }

  @Override
  public Integer updateAuthRoleRelation(AuthRoleRelationEntity authRoleRelation) {
    return authRoleRelationMapper.updateRelation(authRoleRelation);
  }
}
