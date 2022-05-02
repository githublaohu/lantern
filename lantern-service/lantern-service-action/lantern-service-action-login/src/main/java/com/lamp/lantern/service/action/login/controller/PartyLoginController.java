package com.lamp.lantern.service.action.login.controller;


import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.service.action.login.incident.IncidentService;
import com.lamp.lantern.service.action.login.incident.LoginIncident;
import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RequestMapping("/partyLogging")
@RestController("partyLoginController")
@Api(tags = {"一方登录"})
public class PartyLoginController {

    @Autowired
    private IncidentService incidentService;

    @RequestMapping(value= "/input")
    @ResponseBody
    public String hello(){

        System.out.println("hello");

        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        LoginIncident incident = builder.build();
        incident.verifyCodeLogin();



        return "jaycase";
    }

    @PostMapping("accountLoginByUserName")
    @ApiOperation(value = "账户密码登录")
    public ResultObjectEnums accountLoginByUserName(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){

        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();
        incident.partyLoginByUserName();

        return incident.getResultObjectEnums();
    }

    @PostMapping("accountLoginByPhone")
    @ApiOperation(value = "手机号密码登录")
    public ResultObjectEnums accountLoginPhone(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){

        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();
        incident.partyLoginByPhone();

        return incident.getResultObjectEnums();
    }

    @PostMapping("accountLoginByEmail")
    @ApiOperation(value = "邮箱账户密码登录")
    public ResultObjectEnums accountLoginByEmail(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){

        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();
        incident.partyLoginByEmail();

        return incident.getResultObjectEnums();
    }

    @PostMapping("verifycodeLogin")
    @ApiOperation(value = "手机验证码登录")
    public ResultObjectEnums verifycodeLogin(UserInfoEntity userInfoEntity, String verifyCode , HttpServletResponse response, HttpServletRequest request){

        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);
        builder.inputVerifyCode(verifyCode);

        LoginIncident incident = builder.build();
        incident.verifyCodeLogin();

        return incident.getResultObjectEnums();
    }


    @PostMapping("sendVerifyCode")
    @ApiOperation(value = "发送手机验证码")
    public ResultObjectEnums sendVerifyCode(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){
        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();

        incident.checkPhoneExist();
        incident.generateVerifyCode();

        return incident.getResultObjectEnums();
    }

    @PostMapping("tokenUserLogin")
    @ApiOperation(value = "用户令牌登录")
    public ResultObjectEnums tokenUserLogin(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){
        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();


        incident.partyLoginByUserName();
        incident.partyLoginByToken();

        return incident.getResultObjectEnums();

    }

    @PostMapping("userLoginError")
    @ApiOperation(value = "用户登录次数错误")
    public ResultObjectEnums userLoginError(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){
        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();
        incident.countLoginErrorTimes();


        return incident.getResultObjectEnums();

    }

    @PostMapping("userLoginErrorIncrease")
    @ApiOperation(value = "用户登录次数错误增加")
    public ResultObjectEnums userLoginErrorIncrease(UserInfoEntity userInfoEntity, HttpServletResponse response, HttpServletRequest request){
        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.userInfoEntity(userInfoEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();
        incident.increaseUserLoginError();


        return incident.getResultObjectEnums();

    }

    @PostMapping("testInsertLoginRecord")
    @ApiOperation(value = "测试插入登录日志")
    public ResultObjectEnums testInsertLoginRecord(LoginRecordEntity loginRecordEntity, HttpServletResponse response, HttpServletRequest request){
        LoginIncident.LoginIncidentBuilder builder = incidentService.createLoginIncidentBuilder();
        builder.loginRecordEntity(loginRecordEntity);
        builder.request(request);
        builder.response(response);

        LoginIncident incident = builder.build();
        incident.testInsertLoginRecord();


        return incident.getResultObjectEnums();

    }



}
