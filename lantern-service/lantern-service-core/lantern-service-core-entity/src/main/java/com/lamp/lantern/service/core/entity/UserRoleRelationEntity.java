package com.lamp.lantern.service.core.entity;

import com.lamp.lantern.plugins.api.mode.UserRoleRelation;
import io.swagger.annotations.Api;
import lombok.Data;

@Data
@Api(value = "UserRoleRelationEntity", description = "用户角色关系实体类")
public class UserRoleRelationEntity extends UserRoleRelation {

}
