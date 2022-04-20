package com.lamp.lantern.serivce.action.login.controller;

import com.lamp.lantern.serivce.action.login.incident.LoginIncident;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.service.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamp.lantern.serivce.action.login.incident.IncidentSerivce;
import com.lamp.lantern.serivce.action.login.incident.LoginIncident.LoginIncidentBuilder;
import com.lamp.lantern.serivce.action.login.LoginPatternEnums;

import
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@Slf4j
@RequestMapping("/partyLogging")
@RestController("partyLoginController")
@Api(tags = {"一方登录"})
public class PartyLoginController {

    @Autowired
    private IncidentSerivce incidentSerivce;


    @PostMapping("accountLogin")
    @ApiOperation(value = "账户密码登录")
    public ResultObject accountLogin( @RequestBody UserInfoEntity userInfoEntity,
                                                      HttpServletResponse response, HttpServletRequest request){

        LoginIncidentBuilder builder = incidentSerivce.createLoginIncidentBuilder();
        builder.loginPattern(LoginPatternEnums.PARTY_ACCOUNT);
        builder.userInfoEntity(userInfoEntity);

        LoginIncident build = builder.build();
        build.partyLogin();
        build.generateToken();



        return build.getResultObject();

        }




    @PostMapping("/phoneLogin")
    @ApiOperation(value = "手机号密码登录")
    public ResultObject phoneLogin(@RequestBody UserInfoEntity userInfoEntity,
        HttpServletResponse response, HttpServletRequest request){

        return accountLogin(userInfoEntity, response, request);

    }


    @PostMapping("/emailLogin")
    @ApiOperation(value = "邮箱密码登录")
    public ResultObject emailLogin(@RequestBody UserInfoEntity userInfoEntity,
                                                   HttpServletResponse response, HttpServletRequest request){

        return accountLogin(userInfoEntity, response, request);

    }


    @PostMapping("/phoneAndVerificationLogin")
    public ResultObject phoneAndVerificationLogin(@RequestBody UserInfoEntity userInfoEntity, @RequestBody String verifyCode,
                                                                  HttpServletResponse response, HttpServletRequest request){


        LoginIncidentBuilder builder = incidentSerivce.createLoginIncidentBuilder();
        builder.loginPattern(LoginPatternEnums.PARTY_VERIFYCODE);
        builder.userInfoEntity(userInfoEntity);

        LoginIncident build = builder.build();

        build.verficationCodeCheck();




        return build.getResultObject();

    }




}
