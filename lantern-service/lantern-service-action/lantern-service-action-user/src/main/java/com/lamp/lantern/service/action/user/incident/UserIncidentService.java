package com.lamp.lantern.service.action.user.incident;

import com.lamp.lantern.service.core.service.UserInfoEntityService;
import com.lamp.lantern.service.action.user.incident.UserIncident.UserIncidentBuilder;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
*   com.lamp.lantern.service.action.user 主要关注的功能是
*       （1）修改用户信息  （完成）
*       （2）主动退出     （完成）
*       （3）踢下线       （完成）
*
* */

@Component
public class UserIncidentService {

    @Reference
    private UserInfoEntityService userInfoEntityService;

    public UserIncident createUserIncidentService(){
        UserIncidentBuilder builder = UserIncident.builder();
        builder.userInfoEntityService(userInfoEntityService);
        return builder.build();
    }


    public UserIncidentBuilder createUserIncidentServiceBuilder(){
        UserIncidentBuilder builder = UserIncident.builder();
        builder.userInfoEntityService(userInfoEntityService);
        return builder;
    }



}
