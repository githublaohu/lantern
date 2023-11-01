package com.lamp.lantern.plugins.api.service;

import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;

public interface LanternUserInfoService {


    /**
     * 注册第三方登录用户，需要传入第三方的一些信息
     *
     * @param userInfo
     * @param platformUserInfo
     * @return
     */
    public UserInfo registerThirdLoginUser(UserInfo userInfo, PlatformUserInfo platformUserInfo);

    public UserInfo checkUserByUserId(UserInfo userInfo);

    public UserInfo checkUserByUserIdAndTriId(UserInfo userInfo, PlatformUserInfo platformUserInfo);

    UserInfo checkUserByTriIdAndAuthchannel(PlatformUserInfo platformUserInfo);
}
