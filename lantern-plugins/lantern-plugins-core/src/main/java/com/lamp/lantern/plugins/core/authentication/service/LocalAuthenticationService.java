package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.LanternAuthCachePool;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.plugins.api.mode.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author laohu
 */
public class LocalAuthenticationService implements AuthenticationService {

    private final LanternAuthCachePool lanternAuthCachePool = new LanternAuthCachePool();


    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        UserInfo userInfo = lanternAuthCachePool.getTokenUserMap().get(authData.getToken());
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();
        if (Objects.nonNull(userInfo)) {
            authenticationServiceResult.setSuccess(true);
            authenticationServiceResult.setUserInfo(userInfo);
        } else {
            authenticationServiceResult.setSuccess(false);
        }
        return authenticationServiceResult;
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        String resource = authData.getResource();
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();
        UserInfo userInfo = authData.getUserInfo();

        if (Objects.isNull(userInfo)) {
            return authenticationServiceResult.setSuccess(false);
        }

        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        List<Role> roles = lanternAuthCachePool.getUserRoleMap().get(userInfo.getUiId());
        if (Objects.isNull(roles)) {
            return authenticationServiceResult.setSuccess(false);
        }
        for (Role role : roles) {
            List<Resources> resources = lanternAuthCachePool.getRoleResourceMap().get(role.getRoleId());
            for (Resources resources1 : resources) {
                //这里判断了资源的id，但是进来的资源是什么?
                if (Objects.equals(resources1.getResourceId().toString(), resource)) {
                    return authenticationServiceResult.setSuccess(true);
                }
            }
        }
        //没有任何的匹配
        return authenticationServiceResult.setSuccess(false);
    }

    public Integer updateTokenUser(Map<String, UserInfo> map){
        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        lanternAuthCachePool.getTokenUserMap().putAll(map);
        return map.size();
    }

    public Integer updateRoleResource(Map<Long, List<Resources>> map){
        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        lanternAuthCachePool.getRoleResourceMap().putAll(map);
        return map.size();
    }

    public Integer updateUserRole(Map<Long, List<Role>> map){
        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        lanternAuthCachePool.getUserRoleMap().putAll(map);
        return map.size();
    }

}
