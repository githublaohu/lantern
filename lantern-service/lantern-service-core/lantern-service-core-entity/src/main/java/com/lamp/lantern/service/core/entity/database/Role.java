package com.lamp.lantern.service.core.entity.database;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "RoleEntity", description = "角色实体类")
public class Role {
    private Long roleId;
    private String roleName;
    private LocalDateTime roleCreateTime;
    private LocalDateTime roleUpdateTime;
    private LocalDateTime roleEndTime;
    private LocalDateTime roleValidTime;
    private String roleDescription;
    private Integer roleIsDelete;


}
