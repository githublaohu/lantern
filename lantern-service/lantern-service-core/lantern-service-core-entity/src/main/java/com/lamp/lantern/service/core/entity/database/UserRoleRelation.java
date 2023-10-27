package com.lamp.lantern.service.core.entity.database;

import io.swagger.annotations.Api;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Api(value = "UserRoleRelationEntity", description = "用户角色关系实体类")
public class UserRoleRelation {
    private Long urrId;
    private Long urrRoleId;
    private Long urrUserId;
    private LocalDateTime urrCreateTime;
    private LocalDateTime urrUpdateTime;
    private LocalDateTime urrEndTime;
    private LocalDateTime urrValidTime;
    private Integer urrIsDelete;
}
