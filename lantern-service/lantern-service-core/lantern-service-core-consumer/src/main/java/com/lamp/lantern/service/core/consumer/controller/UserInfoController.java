package com.lamp.lantern.service.core.consumer.controller;


import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.consumer.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequestMapping("/userInfo")
@RestController("userInfoController")
@Api(tags = {"用户信息实体接口"})
public class UserInfoController {

    private ResultObjectEnums resultObjectEnums;

    @Reference(url="127.0.0.1:20880")
    private UserInfoService userInfoEntityService;



    @PostMapping(value= "/input")
    @ResponseBody
    public String hello(){
        Integer userInfoEntity = userInfoEntityService.queryUserById();
        System.out.println(userInfoEntity);

        UserInfo userInfoEntity1 = userInfoEntityService.testQuery();
        System.out.println(userInfoEntity1);

        return "jaycase";
    }
//    @GetMapping(value = "/test")
//    @ApiOperation(value = "测试")
//    public String test(){
//        return "test";
//    }

    @PostMapping(value = "checkUserExistByUserName")
    @ApiOperation(value = "用户名是否被注册")
    public ResultObjectEnums checkUserExistByUserName(@RequestBody  UserInfoEntity userInfoEntity){
        // 如果被注册 则返回FAIL, 否则返回SUCCESS
        if(userInfoEntity.getUiName() == null || userInfoEntity.getUiName() == ""){
            resultObjectEnums =  ResultObjectEnums.FAIL;
        }
        UserInfo queryUserInfoEntity = userInfoEntityService.checkUserExistByUserName(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
        }else{
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        return resultObjectEnums;
    }

    @PostMapping(value = "checkUserExistByEmail")
    @ApiOperation(value = "邮箱是否被注册")
    public ResultObjectEnums checkUserExistByEmail(@RequestBody UserInfoEntity userInfoEntity){
        // 如果被注册 则返回FAIL, 否则返回SUCCESS
        if(userInfoEntity.getUiEmail() == null || userInfoEntity.getUiEmail() == ""){
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        UserInfo queryUserInfoEntity = userInfoEntityService.checkUserExistByEmail(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
        }else{
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        return resultObjectEnums;
    }

    @PostMapping(value = "checkUserExistByPhone")
    @ApiOperation(value = "手机号是否被注册")
    public ResultObjectEnums checkUserExistByPhone(@RequestBody UserInfoEntity userInfoEntity){
        // 如果被注册 则返回FAIL, 否则返回SUCCESS
        if(userInfoEntity.getUiPhone() == null || userInfoEntity.getUiPhone() == ""){
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        UserInfo queryUserInfoEntity = userInfoEntityService.checkUserExistByPhone(userInfoEntity);
        if(queryUserInfoEntity == null){
            resultObjectEnums = ResultObjectEnums.SUCCESS;
        }else{
            resultObjectEnums = ResultObjectEnums.FAIL;
        }
        return resultObjectEnums;
    }


    @PostMapping(value = "registerUser")
    @ApiOperation(value = "注册用户")
    public ResultObjectEnums registerUserInfoEntity(@RequestBody UserInfoEntity userInfoEntity){

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


        UserInfoEntity userInfo = userInfoEntityService.registerUserInfoEntity(userInfoEntity);

        if(Objects.nonNull(userInfo)){
            return ResultObjectEnums.SUCCESS;
        }else{
            return ResultObjectEnums.FAIL;
        }

    }

    @PostMapping(value = "accountLoginUser")
    @ApiOperation(value = "用户登录")
    public ResultObjectEnums AccountloginUserInfoEnity(@RequestBody UserInfoEntity userInfoEntity){
        userInfoEntity.setUiName("jaycase");

        UserInfo queryUserInfoEntity = userInfoEntityService.queryUserByUserName(userInfoEntity);

        System.out.println("queryUser " + queryUserInfoEntity);

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
    @PostMapping(value = "deleteUser")
    @ApiOperation(value = "删除用户")
    public ResultObjectEnums deleteUser(@RequestBody UserInfoEntity userInfoEntity){
        Integer integer = userInfoEntityService.deleteUser(userInfoEntity);
        return ResultObjectEnums.SUCCESS;
    }

//    @PostMapping("testInsertLoginRecord")
//    @ApiOperation(value = "测试插入登录日志")
//    public ResultObjectEnums testInsertLoginRecord(LoginRecordEntity loginRecordEntity, HttpServletResponse response, HttpServletRequest request){
//
//        Integer integer = loginRecordEntityService.insertLoginRecord(loginRecordEntity);
//
//        System.out.println(integer);
//
//        System.out.println(loginRecordEntity.getUlId());
//
//        return ResultObjectEnums.SUCCESS;
//
//    }





}
