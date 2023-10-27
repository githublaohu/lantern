package com.lamp.lantern.plugins.api.service;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface LanternUserInfoService {



    public UserInfo registerUserInfoEntity(UserInfo userInfo);

    public UserInfo checkUser(UserInfo userInfo);

}
