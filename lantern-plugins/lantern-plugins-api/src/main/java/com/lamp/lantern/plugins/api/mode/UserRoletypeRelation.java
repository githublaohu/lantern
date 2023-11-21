package com.lamp.lantern.plugins.api.mode;

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
