package com.lamp.lantern.plugins.api.mode;


import java.io.Serializable;
import java.time.LocalDateTime;

import com.lamp.lantern.plugins.api.injection.OperateInfoInjection;
import lombok.Data;

@Data
public class ResourceRoleRelation extends OperateInfoInjection implements Serializable {

    private Long rrrId;

    private Long rrrResourceId;

    private Long rrrRoleId;

    private String rrrType;

    private Long rrrTypeId;

    private LocalDateTime rrrCreateTime;

    private LocalDateTime rrrUpdateTime;

    private LocalDateTime rrrEndTime;

    private LocalDateTime rrrValidTime;

    private LocalDateTime rrrStartTime;

    private Long rrrCreateUserId;

    private Long rrrUpdateUserId;

    private Integer isDelete;

}
