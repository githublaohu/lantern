package com.lamp.lantern.service.action.login.security;

import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface LoginErrorCountService {

    int countUserLoginError(UserInfoEntity userInfoEntity);

    void refershUserLoginError(UserInfoEntity userInfoEntity);

    void insertUserLoginError(UserInfoEntity userInfoEntity);

}
