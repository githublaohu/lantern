package com.lamp.lantern.plugins.api.mode;


import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ResourceRoleRelation implements Serializable {

    private Long rrrId;

    private Long rrrResourceId;

    private Long rrrOperatorId;

    private Long rrrRoleId;

    private String rrrType;

    private Long rrrTypeId;

    private LocalDateTime rrrCreateTime;

    private LocalDateTime rrrUpdateTime;

    private LocalDateTime rrrEndTime;

    private LocalDateTime rrrValidTime;

    private Integer rrrIsDelete;

}
