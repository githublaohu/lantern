package com.lamp.lantern.service.core.consumer.controller;

import com.lamp.lantern.service.core.consumer.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.database.Role;
import com.lamp.lantern.service.core.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/role")
@RestController("roleController")
@Api(tags = {"身份管理接口"})
public class RoleController {
    @Reference(url = "127.0.0.1:20880")
    private RoleService roleService;

    @ApiOperation(value = "插入新身份")
    @PostMapping(value = "insertRole")
    public ResultObjectEnums insertRole(@RequestBody RoleEntity roleEntity) {
        Integer integer = roleService.insertRole(roleEntity);
        return ResultObjectEnums.SUCCESS;
    }
    @ApiOperation(value = "批量插入新身份")
    @PostMapping(value = "insertRoleBatch")
    public ResultObjectEnums insertRoleBatch(@RequestBody List<RoleEntity> roleEntityList) {
        Integer integer = roleService.insertRoles(roleEntityList);
        return ResultObjectEnums.SUCCESS;
    }

    @ApiOperation(value = "按照用户id获取所有有效身份")
    @PostMapping(value = "getRolesByUserId")
    public List<Role> getRolesByUserId(@RequestBody UserInfoEntity userInfoEntity) {
        return roleService.getAllRoleByUserId(userInfoEntity);
    }

}
