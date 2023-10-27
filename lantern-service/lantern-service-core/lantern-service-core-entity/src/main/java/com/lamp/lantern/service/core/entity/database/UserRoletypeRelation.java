package com.lamp.lantern.service.core.entity.database;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRoletypeRelation {
    private Long utrId;
    private Long utrUserId;
    private Long utrRoletypeId;
    private LocalDateTime utrCreateTime;
    private LocalDateTime utrUpdateTime;
    private LocalDateTime utrEndTime;
    private LocalDateTime utrValidTime;
}
