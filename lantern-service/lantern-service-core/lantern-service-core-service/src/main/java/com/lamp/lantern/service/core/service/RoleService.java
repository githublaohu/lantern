package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import java.util.List;

public  interface RoleService {

     List<RoleEntity> queryByForm(RoleEntity roleEntity);
    
    RoleEntity queryByRoleId(RoleEntity roleEntity);

     Integer insertRole(RoleEntity roleEntity);

     Integer updateRole(RoleEntity roleEntity);

     List<RoleEntity> getValidRoles();

     Integer checkRoleValid(RoleEntity roleEntity);

     Integer endRole(RoleEntity roleEntity);

     Integer endRoles(List<RoleEntity> roleEntities);

    /**
     * 查询UserRole和UserRoletype两种关系
     * 这个函数会获取到关系表/角色表中valid_time不正确的数据
     *
     * @param userInfoEntity
     * @return
     */
     List<RoleEntity> getAllRoleByUserId(UserInfoEntity userInfoEntity);
    /**
     * 查询UserRole和UserRoletype两种关系
     * 这个函数会获取到关系表/角色表中valid_time都正确的数据
     *
     * @param userInfoEntity
     * @return
     */
     List<RoleEntity> getAllValidRoleByUserId(UserInfoEntity userInfoEntity);
}
