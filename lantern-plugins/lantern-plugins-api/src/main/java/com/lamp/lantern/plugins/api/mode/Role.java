package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "RoleEntity", description = "角色实体类")
public class Role extends OperateInfoInjection {

    private Long roleId;

    private Long roleTypeId;

    private Long systemId;

    private Long productId;

    private Long projectId;

    private String projectName;

    private String roleType;

    private String roleName;

    private String roleDescription;

    private String roleTag;

    private Integer isDelete;


}
