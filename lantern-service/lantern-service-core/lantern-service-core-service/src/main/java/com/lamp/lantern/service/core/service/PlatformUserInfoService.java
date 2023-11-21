package com.lamp.lantern.service.core.service;


import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.PlatformUserInfoEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;


public interface PlatformUserInfoService {
    Integer registerPlatformUserInfo(PlatformUserInfoEntity platformUserInfo);


    UserInfo checkUserByUserIdAndTriId(PlatformUserInfoEntity platformUserInfoEntity);

    UserInfoEntity checkUserByTriIdAndAuthchannel(PlatformUserInfoEntity platformUserInfoEntity);
}
