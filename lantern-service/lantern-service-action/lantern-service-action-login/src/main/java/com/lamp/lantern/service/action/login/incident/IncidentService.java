package com.lamp.lantern.service.action.login.incident;


import com.lamp.lantern.service.action.login.security.HttpSessionService;
import com.lamp.lantern.service.action.login.security.JwtTokenService;
import com.lamp.lantern.service.action.login.security.LoginErrorCountService;
import com.lamp.lantern.service.action.login.security.RedisService;
import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.enums.LoginPatternEnum;
import com.lamp.lantern.service.core.service.LoginRecordEntityService;
import org.springframework.stereotype.Component;
import org.apache.dubbo.config.annotation.Reference;

import com.lamp.lantern.service.core.service.UserInfoEntityService;
import com.lamp.lantern.service.action.login.incident.LoginIncident.LoginIncidentBuilder;

import com.lamp.lantern.service.action.login.incident.TriPartiteIncident.TriPartiteIncidentBuilder;

@Component
public class IncidentService {

    // 采用构造者模式

    @Reference
    private UserInfoEntityService userInfoEntityService;

    @Reference
    private LoginRecordEntityService loginRecordEntityService;

    @Reference
    private RedisService redisService;

    @Reference
    private JwtTokenService jwtTokenService;

    @Reference
    private LoginErrorCountService loginErrorCountService;

    @Reference
    private HttpSessionService httpSessionService;


    public LoginIncident createLoginIncident(){
        LoginIncidentBuilder builder = LoginIncident.builder();
        builder.redisService(redisService);
        builder.jwtTokenService(jwtTokenService);
        builder.userInfoEntityService(userInfoEntityService);
        builder.httpSessionService(httpSessionService);
        builder.loginErrorCountService(loginErrorCountService);
        builder.loginRecordEntityService(loginRecordEntityService);
        return builder.build();
    }

    public LoginIncidentBuilder createLoginIncidentBuilder(){
        LoginIncidentBuilder builder = LoginIncident.builder();
        builder.redisService(redisService);
        builder.jwtTokenService(jwtTokenService);
        builder.httpSessionService(httpSessionService);
        builder.userInfoEntityService(userInfoEntityService);
        builder.loginErrorCountService(loginErrorCountService);
        builder.loginRecordEntityService(loginRecordEntityService);
        return builder;
    }

    public TriPartiteIncident createTriPartiteIncident(){
        TriPartiteIncidentBuilder builder = TriPartiteIncident.builder();
        builder.redisService(redisService);
        builder.userInfoEntityService(userInfoEntityService);
        builder.httpSessionService(httpSessionService);
        builder.resultObjectEnums(ResultObjectEnums.SUCCESS);
        builder.loginRecordEntityService(loginRecordEntityService);
        builder.loginPatternEnum(LoginPatternEnum.TRIPARTITE_WEIXIN_SCAN);
        builder.loginRecordEntity(new LoginRecordEntity());

        return builder.build();
    }

    public TriPartiteIncidentBuilder createTriPartiteIncidentBuilder(){
        TriPartiteIncidentBuilder builder = TriPartiteIncident.builder();
        builder.redisService(redisService);
        builder.userInfoEntityService(userInfoEntityService);
        builder.httpSessionService(httpSessionService);
        builder.resultObjectEnums(ResultObjectEnums.SUCCESS);
        builder.loginRecordEntityService(loginRecordEntityService);
        builder.loginPatternEnum(LoginPatternEnum.TRIPARTITE_WEIXIN_SCAN);
        builder.loginRecordEntity(new LoginRecordEntity());

        return builder;
    }


}
