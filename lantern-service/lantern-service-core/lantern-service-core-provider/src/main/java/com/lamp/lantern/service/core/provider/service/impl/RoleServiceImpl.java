package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.provider.mapper.RoleMapper;
import com.lamp.lantern.service.core.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class RoleServiceImpl implements RoleService {
  @Autowired
  private RoleMapper roleMapper;

  @Override
  public List<RoleEntity> queryByForm(RoleEntity roleEntity) {
    return roleMapper.queryByForm(roleEntity);
  }

  @Override
  public Integer insertRole(RoleEntity roleEntity) {
    return roleMapper.insertRole(roleEntity);
  }

  @Override
  public Integer updateRole(RoleEntity roleEntity) {
    return roleMapper.updateRole(roleEntity);
  }
}
