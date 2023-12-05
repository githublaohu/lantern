package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import io.swagger.annotations.Api;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Api(value = "UserRoleRelationEntity", description = "用户角色关系实体类")
public class UserRoleRelation extends OperateInfoInjection {

    private Long urrId;

    private String urrType;

    private Long roleId;

    private Long userId;

    private LocalDateTime urrCreateTime;

    private LocalDateTime urrUpdateTime;

    private LocalDateTime urrEndTime;

    private LocalDateTime urrValidTime;

    private LocalDateTime urrStartTime;

    private Long urrCreateUserId;

    private Long urrUpdateUserId;

    private Integer isDelete;

}
