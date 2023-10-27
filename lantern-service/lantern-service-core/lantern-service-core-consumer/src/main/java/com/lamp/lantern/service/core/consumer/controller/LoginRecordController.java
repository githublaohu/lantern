package com.lamp.lantern.service.core.consumer.controller;

import com.lamp.lantern.service.core.consumer.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.database.LoginRecord;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.LoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/loginRecord")
@RestController("loginRecordController")
@Api(tags = {"登录日志实体接口"})
public class LoginRecordController {
    @Reference(url = "127.0.0.1:20880")
    private LoginRecordService loginRecordEntityService;


    @ApiOperation(value = "插入登录日志")
    public ResultObjectEnums insertLoginRecord(LoginRecordEntity loginRecordEntity) {

        Integer integer = loginRecordEntityService.insertLoginRecord(loginRecordEntity);

        return ResultObjectEnums.SUCCESS;

    }

    @ApiOperation(value = "按照用户id查询登录日志")
    @PostMapping(value = "checkLoginRecordByUserId")
    public List<LoginRecord> checkLoginRecordByUserId(UserInfoEntity userInfoEntity) {
        return loginRecordEntityService.checkLoginRecordByUserId(userInfoEntity);
    }

    @ApiOperation(value = "更新退出时间")
    @PostMapping(value = "updateLoginRecordExitTimeField")
    public ResultObjectEnums updateLoginRecordExitTimeField(LoginRecordEntity loginRecordEntity) {
        Integer integer = loginRecordEntityService.updateLoginRecordExitTimeField(loginRecordEntity);
        return ResultObjectEnums.SUCCESS;
    }

    @ApiOperation(value = "更新退出方式")
    @PostMapping(value = "updateLoginRecordQuitWayField")
    public ResultObjectEnums updateLoginRecordQuitWayField(LoginRecordEntity loginRecordEntity) {
        Integer integer = loginRecordEntityService.updateLoginRecordQuitWayField(loginRecordEntity);
        return ResultObjectEnums.SUCCESS;
    }
}

