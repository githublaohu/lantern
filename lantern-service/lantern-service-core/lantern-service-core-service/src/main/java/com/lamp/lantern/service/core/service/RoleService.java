package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.plugins.api.mode.Role;

import java.util.List;

public interface RoleService {

    public List<Role> queryByForm(RoleEntity roleEntity);

    public Integer insertRole(RoleEntity roleEntity);

    public Integer insertRoles(List<RoleEntity> roleEntities);

    public Integer updateRole(RoleEntity roleEntity);

    public List<Role> getValidRoles();

    public Integer checkRoleValid(RoleEntity roleEntity);

    public Integer endRole(RoleEntity roleEntity);

    public Integer endRoles(List<RoleEntity> roleEntities);

    /**
     * 查询UserRole和UserRoletype两种关系
     * 这个函数会获取到关系表/角色表中valid_time不正确的数据
     *
     * @param userInfoEntity
     * @return
     */
    public List<Role> getAllRoleByUserId(UserInfoEntity userInfoEntity);
    /**
     * 查询UserRole和UserRoletype两种关系
     * 这个函数会获取到关系表/角色表中valid_time都正确的数据
     *
     * @param userInfoEntity
     * @return
     */
    public List<Role> getAllValidRoleByUserId(UserInfoEntity userInfoEntity);
}
