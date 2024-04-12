package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
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

  public RoleEntity queryByRoleId(RoleEntity roleEntity){

    return roleMapper.queryByRoleId(roleEntity);
  }

  @Override
  public List<RoleEntity> queryByForm(RoleEntity roleEntity) {
    return roleMapper.queryByForm(roleEntity); }

  @Override
  public Integer insertRole(RoleEntity roleEntity) {
     roleMapper.insertRole(roleEntity);
     return roleEntity.getRoleId().intValue();
  }

  @Override
  public Integer updateRole(RoleEntity roleEntity) {
    return roleMapper.updateRole(roleEntity);
  }

  @Override
  public List<RoleEntity> getValidRoles() {
    return roleMapper.getValidRoles();
    }

  /**
   * 有效为1，无效为0
   * @param roleEntity
   * @return
   */
  @Override
  public Integer checkRoleValid(RoleEntity roleEntity) {
      return roleMapper.checkRoleValid(roleEntity);
  }

  @Override
  public Integer endRole(RoleEntity roleEntity) {
    return roleMapper.endRole(roleEntity);
  }

  @Override
  public Integer endRoles(List<RoleEntity> roleEntities) {
    return roleMapper.endRoles(roleEntities);
  }

  @Override
  public List<RoleEntity> getAllRoleByUserId(UserInfoEntity userInfoEntity) {
    return roleMapper.getAllRoleByUserId(userInfoEntity);}

  @Override
  public List<RoleEntity> getAllValidRoleByUserId(UserInfoEntity userInfoEntity) {
    return roleMapper.getAllValidRoleByUserId(userInfoEntity);}

}
