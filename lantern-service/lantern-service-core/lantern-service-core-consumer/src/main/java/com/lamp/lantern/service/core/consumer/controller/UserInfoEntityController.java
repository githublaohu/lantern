package com.lamp.lantern.service.core.consumer.controller;


import com.lamp.lantern.service.core.consumer.utils.JwtAuthenticationUtil;
import com.lamp.lantern.service.core.consumer.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.LoginRecordEntityService;
import com.lamp.lantern.service.core.service.UserInfoEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequestMapping("/userInfo")
@RestController("userInfoController")
@Api(tags = {"用户信息实体接口"})
public class UserInfoEntityController {

    private ResultObjectEnums resultObjectEnums;

    @Reference
    private UserInfoEntityService userInfoEntityService;

    @Reference
    private LoginRecordEntityService loginRecordEntityService;

    @RequestMapping(value= "/input")
    @ResponseBody
    public String hello(){
        Integer userInfoEntity = userInfoEntityService.quertUserById();
        System.out.println(userInfoEntity);

        UserInfoEntity userInfoEntity1 = userInfoEntityService.testQuery();
        System.out.println(userInfoEntity1);


        return "jaycase";
    }

    @RequestMapping(value = "checkUserExistByUserName")
    @ApiOperation(value = "用户名是否被注册")
    public ResultObjectEnums checkUserExistByUserName(UserInfoEntity userInfoEntity){
        // 如果被注册 则返回FAIL, 否则返回SUCCESS
        if(userInfoEntity.getUiName() == null || userInfoEntity.getUiName() == ""){
            resultObjectEnums =  ResultObjectEnums.FAIL;
        }
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByUserName(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
        }else{
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        return resultObjectEnums;
    }

    @RequestMapping(value = "checkUserExistByEmail")
    @ApiOperation(value = "邮箱是否被注册")
    public ResultObjectEnums checkUserExistByEmail(UserInfoEntity userInfoEntity){
        // 如果被注册 则返回FAIL, 否则返回SUCCESS
        if(userInfoEntity.getUiEmail() == null || userInfoEntity.getUiEmail() == ""){
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByEmail(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
        }else{
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        return resultObjectEnums;
    }

    @RequestMapping(value = "checkUserExistByPhone")
    @ApiOperation(value = "手机号是否被注册")
    public ResultObjectEnums checkUserExistByPhone(UserInfoEntity userInfoEntity){
        // 如果被注册 则返回FAIL, 否则返回SUCCESS
        if(userInfoEntity.getUiPhone() == null || userInfoEntity.getUiPhone() == ""){
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        UserInfoEntity queryUserInfoEntity = userInfoEntityService.checkUserExistByPhone(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
        }else{
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        return resultObjectEnums;
    }


    @RequestMapping(value = "registerUser")
    @ApiOperation(value = "注册用户")
    public ResultObjectEnums registerUserInfoEntity(UserInfoEntity userInfoEntity){

        checkUserExistByUserName(userInfoEntity);
        checkUserExistByEmail(userInfoEntity);
        checkUserExistByPhone(userInfoEntity);

        if(Objects.equals(ResultObjectEnums.FAIL, resultObjectEnums)){
            return resultObjectEnums;
        }

        String uiSalt = UUID.randomUUID().toString().replace("-","") + userInfoEntity.getUiName().substring(2,3);

        System.out.println("salt"+ uiSalt);
        userInfoEntity.setUiSalt(uiSalt);


        String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());
        userInfoEntity.setUiSaltPassword(uiSaltPassword);

        System.out.println("token" + JwtAuthenticationUtil.createToken(userInfoEntity.getUiName()).length());

        userInfoEntity.setUiToken(JwtAuthenticationUtil.createToken(userInfoEntity.getUiName()));
        int status = userInfoEntityService.registerUserInfoEntity(userInfoEntity);

        if(status == 1){
            return ResultObjectEnums.SUCCESS;
        }else{
            return ResultObjectEnums.FAIL;
        }

    }

    @RequestMapping(value = "accountLoginUser")
    @ApiOperation(value = "用户登录")
    public ResultObjectEnums AccountloginUserInfoEnity(UserInfoEntity userInfoEntity){
        userInfoEntity.setUiName("jaycase");

        UserInfoEntity queryUserInfoEntity = userInfoEntityService.queryUserByUserName(userInfoEntity);

        System.out.println("querUser " + queryUserInfoEntity);

        if(queryUserInfoEntity == null){
            return ResultObjectEnums.FAIL;
        }

        String uiSalt = queryUserInfoEntity.getUiSalt();
        String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());

        System.out.println("password " + uiSaltPassword);
        System.out.println("UiSaltPassword " + queryUserInfoEntity.getUiSaltPassword());
        if(Objects.equals(uiSaltPassword, queryUserInfoEntity.getUiSaltPassword())){
            return ResultObjectEnums.SUCCESS;
        }else{
            return ResultObjectEnums.FAIL;
        }

    }

    @PostMapping("testInsertLoginRecord")
    @ApiOperation(value = "测试插入登录日志")
    public ResultObjectEnums testInsertLoginRecord(LoginRecordEntity loginRecordEntity, HttpServletResponse response, HttpServletRequest request){

        Integer integer = loginRecordEntityService.insertLoginRecord(loginRecordEntity);

        System.out.println(integer);

        System.out.println(loginRecordEntity.getUlId());



        return ResultObjectEnums.SUCCESS;

    }


}
