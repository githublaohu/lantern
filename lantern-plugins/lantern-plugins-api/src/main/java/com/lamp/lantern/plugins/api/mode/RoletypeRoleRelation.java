package com.lamp.lantern.plugins.api.mode;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoletypeRoleRelation {
    private Long trrId;
    private Long trrRoletypeId;
    private Long trrRoleId;
    private LocalDateTime trrCreateTime;
    private LocalDateTime trrEndTime;
    private LocalDateTime trrValidTime;
    private LocalDateTime trrUpdateTime;
}
