package com.lamp.lantern.service.action.user.controller;


import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.service.action.user.incident.UserIncident;
import com.lamp.lantern.service.action.user.incident.UserIncidentService;
import com.lamp.lantern.service.action.user.incident.UserIncident.UserIncidentBuilder;
import com.lamp.lantern.service.action.user.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.UserInfoEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
* （1）相关操作
*       修改用户相关信息：头像，密码，昵称
*       敏感信息如IdCard等除非为空否则无法修改
*
*       Session相关的
*       退出 强制下线等
*
* */


@Slf4j
@RequestMapping("/partyUserController")
@RestController("partyUserController")
@Api(tags = {"一方用户操作接口"})
public class PartyUserController {

    @Autowired
    private UserIncidentService userIncidentService;

    @PostMapping("changeUserPassword")
    @ApiOperation(value = "修改密码")
    public ResultObjectEnums changeUserPassword(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){

        UserIncidentBuilder builder = userIncidentService.createUserIncidentServiceBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        UserIncident incident = builder.build();
        incident.changeUserPassword();

        return incident.getResultObjectEnums();
    }

    @PostMapping("changeUserHeadPortrait")
    @ApiOperation(value = "修改图像")
    public ResultObjectEnums changeUserHeadPortrait(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){

        UserIncidentBuilder builder = userIncidentService.createUserIncidentServiceBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        UserIncident incident = builder.build();
        incident.changeUserHeadPortrait();

        return incident.getResultObjectEnums();
    }

    @PostMapping("changeUserNickName")
    @ApiOperation(value = "修改用户昵称")
    public ResultObjectEnums changeUserNickname(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){
        UserIncidentBuilder builder = userIncidentService.createUserIncidentServiceBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        UserIncident incident = builder.build();
        incident.changeUserNickname();
        return incident.getResultObjectEnums();
    }


    @PostMapping("logout")
    @ApiOperation(value = "用户退出")
    public ResultObjectEnums logout(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){
        UserIncidentBuilder builder = userIncidentService.createUserIncidentServiceBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        UserIncident incident = builder.build();
        incident.logout();
        return incident.getResultObjectEnums();

    }





}
