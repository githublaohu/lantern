package com.lamp.lantern.serivce.action.login.security;

import com.lamp.lantern.service.core.entity.UserInfoEntity;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalUserLoginErrorCount implements UserLoginErrorCount{

    // 第一个key 是时间  第二个key是系统id
    private Map<String, Map<String, LoginErrorRecord>> loginErrorRecords = new ConcurrentHashMap<>();

    // 校验登录次数
    // 累计登录次数
    // 重置登录次数

    public Integer getUserLoginErrorLoginCount(UserInfoEntity userInfoEntity){
        for(String key : loginErrorRecords.keySet()){

            LoginErrorRecord systemIdLoginErrorRecord = loginErrorRecords.get(new Date().toString()).get(userInfoEntity.getTriId().toString());

        }


        return 0;


    }

    public void refershLoginErroeLoginCount(UserInfoEntity userInfoEntity){

        //  Map 对记录的删除


    }


    @Override
    public Integer getUserLoginCount(UserInfoEntity userInfoEntity) {

    }


    public

    static class  LoginErrorRecord{

        private Map<String, AtomicInteger> records = new ConcurrentHashMap<>();
    }



}
