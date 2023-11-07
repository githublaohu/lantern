package com.lamp.lantern.service.core.entity;

import com.lamp.lantern.plugins.api.mode.Role;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "RoleEntity", description = "角色实体类")
public class RoleEntity extends Role implements Serializable {

}
