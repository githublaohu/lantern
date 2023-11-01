package com.lamp.lantern.plugins.core.login;

import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.LanternUserInfoService;

public class LanternUserInfoServiceMock implements LanternUserInfoService {
    public UserInfo registerThirdLoginUser(UserInfo userInfo, PlatformUserInfo platformUserInfo) {
       return null;
    }

    @Override
    public UserInfo checkUserByUserId(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo checkUserByUserIdAndTriId(UserInfo userInfo, PlatformUserInfo platformUserInfo) {
        return null;
    }

    @Override
    public UserInfo checkUserByTriIdAndAuthchannel(PlatformUserInfo platformUserInfo) {
        return null;
    }


    public UserInfo checkUserByUserIdAndTriId(UserInfo userInfo) {
        return null;
    }
}
