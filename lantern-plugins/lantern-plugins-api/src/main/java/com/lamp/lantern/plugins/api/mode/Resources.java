package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import io.swagger.annotations.Api;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Api(value = "ResourcesEntity", description = "资源实体类")
public class Resources extends OperateInfoInjection {

    private Long resourceId;

    private Long resourceSystemId;

    private Long resourceProjectId;

    private String resourceProjectName;

    private Long resourceModuleId;

    private String resourceModuleName;

    private String resourceType;

    private String resourceName;

    private LocalDateTime resourceCreateTime;

    private LocalDateTime resourceUpdateTime;

    private LocalDateTime resourceEndTime;

    private LocalDateTime resourceValidTime;

    private Integer resourceIsDelete;

    private String resourceOperator;

    private String resourceConditions;

    private Long resourceParentResourceId;

    private String resourceDescription;

}
