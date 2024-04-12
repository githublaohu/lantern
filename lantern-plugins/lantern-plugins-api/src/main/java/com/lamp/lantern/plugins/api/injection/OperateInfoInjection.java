package com.lamp.lantern.plugins.api.injection;

import com.lamp.lantern.plugins.api.annotation.UserInjection;
import lombok.Data;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDateTime;

@Data
public class OperateInfoInjection {
    /**
     * 插入和更新时，操作人id
     */
    @UserInjection("uiId")
    private Long operatorId;
    /**
     * 仅查询时使用
     */
    private Long createUserId;
    /**
     * 仅查询时使用
     */
    private Long updateUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime endTime;

    private LocalDateTime validTime;

    private LocalDateTime startTime;

}
