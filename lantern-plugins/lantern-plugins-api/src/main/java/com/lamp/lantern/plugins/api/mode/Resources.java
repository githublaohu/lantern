package com.lamp.lantern.plugins.api.mode;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import io.swagger.annotations.Api;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Api(value = "ResourcesEntity", description = "资源实体类")
public class Resources extends OperateInfoInjection {

    private Long resourceId;

    private Long systemId;

    private Long productId;

    private Long projectId;

    private String projectName;

    private Long moduleId;

    private String moduleName;

    private String resourceType;

    private String resourceVersion;

    private String resourceName;

    private String resourceDescription;

    private String resourceTag;

    private String resourceOperate;

    private String resourceConditions;

    private Long resourceParentResourceId;

    private Integer isDelete;

}
