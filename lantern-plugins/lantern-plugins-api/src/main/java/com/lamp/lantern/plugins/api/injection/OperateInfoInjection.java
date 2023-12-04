package com.lamp.lantern.plugins.api.injection;

import com.lamp.lantern.plugins.api.annotation.UserInjection;
import lombok.Data;
import org.springframework.scheduling.support.SimpleTriggerContext;

@Data
public class OperateInfoInjection {
    @UserInjection("uiId")
    private String operatorId;
}
