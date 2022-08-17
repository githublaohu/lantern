package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.RoleEntity;

import java.util.List;

public interface RoleService {

  public List<RoleEntity> queryByForm(RoleEntity roleEntity);

  public Integer insertRole(RoleEntity roleEntity);

  public Integer updateRole(RoleEntity roleEntity);
}
