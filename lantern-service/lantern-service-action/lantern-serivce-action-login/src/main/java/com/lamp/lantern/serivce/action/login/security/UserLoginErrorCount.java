package com.lamp.lantern.serivce.action.login.security;

import com.lamp.lantern.service.core.entity.UserInfoEntity;



public interface UserLoginErrorCount  {

    // 校验用户登录次数
    public Integer getUserLoginCount(UserInfoEntity userInfoEntity);

}



