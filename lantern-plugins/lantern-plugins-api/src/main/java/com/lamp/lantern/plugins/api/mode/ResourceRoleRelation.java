package com.lamp.lantern.plugins.api.mode;


import java.io.Serializable;
import java.time.LocalDateTime;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import lombok.Data;

@Data
public class ResourceRoleRelation extends OperateInfoInjection implements Serializable {

    private Long rrrId;

    private Long resourceId;

    private Long roleId;

    private String rrrType;

    private Long rrrTypeId;

    private Integer isDelete;

}
