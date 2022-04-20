package com.lamp.lantern.service.core.consumer.controller;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.consumer.utils.ResultObjectEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.lamp.decoration.core.result.ResultObject;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;


import com.lamp.lantern.service.core.service.UserInfoService;

@Slf4j
@RequestMapping("/userInfo")
@RestController("userInfoController")
@Api(tags = {"用户信息实体接口"})
public class UserInfoEntityController {


    @Reference
    private UserInfoService userInfoService;

    @RequestMapping(value= "/input")
    @ResponseBody
    public String hello(){
        System.out.println(userInfoService);

        return "jaycase";
    }

    /**
     * 添加用户
     * @param userInfoEntity
     */
    @PostMapping("/insertUser")
    @ApiOperation(value = "添加用户")
    public ResultObject<String> insertUser(@RequestBody UserInfoEntity userInfoEntity){
        System.out.println(userInfoService);
        System.out.println("--------------");
        System.out.println(userInfoEntity);
        userInfoService.insertUserInfoEntity(userInfoEntity);




        return ResultObjectEnums.SUCCESS.getResultObject();

    }
}
