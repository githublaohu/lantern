package com.lamp.lantern.service.action.login.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.service.action.login.handler.HandlerExecute;
import com.lamp.lantern.service.action.login.handler.HandlerService;

import com.lamp.lantern.service.core.entity.UserInfoEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RequestMapping("/partyLogging")
@RestController("partyLoginController")
@Api(tags = {"一方登录"})
public class PartyLoginController {

	@Autowired
	private HandlerService handlerService;
	
    @PostMapping("accountLogin")
    @ApiOperation(value = "账户密码登录")
    public ResultObject<Object> accountLogin(UserInfoEntity userInfoEntity){
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(userInfoEntity);
        return handlerExecute.execute();
    }

    @PostMapping("sendVerifyCode")
    @ApiOperation(value = "发送手机验证码")
    public ResultObject<Object> sendVerifyCode(UserInfoEntity userInfoEntity){
    	 HandlerExecute handlerExecute = handlerService.getHandlerExecute(userInfoEntity);
        return handlerExecute.execute();
    }
}
