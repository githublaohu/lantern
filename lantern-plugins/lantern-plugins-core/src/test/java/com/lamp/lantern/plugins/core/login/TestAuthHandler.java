package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestAuthHandler extends AbstractAuthHandler {
    public ResultObject<String> authBefore(UserInfo userInfo){
        log.info("authBefore");
        log.info("userInfo:{}",userInfo);
        return null ;
    }
    public ResultObject<String> authAfter(UserInfo userInfo){
        log.info("authAfter");
        log.info("userInfo:{}",userInfo);
        return null ;
    }

}
