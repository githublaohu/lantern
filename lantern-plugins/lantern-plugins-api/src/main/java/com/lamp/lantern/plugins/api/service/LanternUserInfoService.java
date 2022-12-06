package com.lamp.lantern.plugins.api.service;

import com.lamp.lantern.plugins.api.mode.UserInfo;

public interface LanternUserInfoService {

    public Integer registerUserInfoEntity(UserInfo userInfo);

    public UserInfo checkUser(UserInfo userInfo);

}
