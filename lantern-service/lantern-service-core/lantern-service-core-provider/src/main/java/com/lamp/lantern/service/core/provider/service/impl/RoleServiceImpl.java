package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.plugins.api.mode.Role;
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

//  @PostConstruct
//  public void test(){
//    RoleEntity roleEntity = new RoleEntity();
//    roleEntity.setRoleId(1l);
//    roleEntity.setRoleName("test");
//    roleEntity.setRoleDescription("test");
//    roleEntity.setRoleValidTime(LocalDateTime.now());
////    this.insertRole(roleEntity);
//    int a = this.checkRoleValid(roleEntity);
//    this.endRole(roleEntity);
//    int b = this.checkRoleValid(roleEntity);
//    var c = this.getValidRoles();
//
//    UserInfoEntity userInfoEntity = new UserInfoEntity();
//    userInfoEntity.setUiId(1l);
//    var d = this.getAllValidRoleByUserId(userInfoEntity);
//
//  }
  @Override
  public List<Role> queryByForm(RoleEntity roleEntity) {
    return roleMapper.queryByForm(roleEntity); }

  @Override
  public Integer insertRole(RoleEntity roleEntity) {
    return roleMapper.insertRole(roleEntity);
  }

  @Override
  public Integer insertRoles(List<RoleEntity> roleEntities) {
    roleEntities.forEach(this::insertRole);
    return 1;
  }

  @Override
  public Integer updateRole(RoleEntity roleEntity) {
    return roleMapper.updateRole(roleEntity);
  }

  @Override
  public List<Role> getValidRoles() {
    List<Role> result = roleMapper.getValidRoles();
    return result;}

  /**
   * 有效为1，无效为0
   * @param roleEntity
   * @return
   */
  @Override
  public Integer checkRoleValid(RoleEntity roleEntity) {
    Long id = roleEntity.getRoleId();
    Integer result = roleMapper.checkRoleValid(id);
    return result;
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
  public List<Role> getAllRoleByUserId(UserInfoEntity userInfoEntity) {
    return roleMapper.getAllRoleByUserId(userInfoEntity);}

  @Override
  public List<Role> getAllValidRoleByUserId(UserInfoEntity userInfoEntity) {
    return roleMapper.getAllValidRoleByUserId(userInfoEntity);}

}
