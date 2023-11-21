package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.LanternAuthCachePool;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.plugins.api.mode.UserInfo;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author laohu
 */
public class LocalAuthenticationService implements AuthenticationService {



    public void setLanternAuthCachePool(LanternAuthCachePool lanternAuthCachePool) {
        this.lanternAuthCachePool = lanternAuthCachePool;
    }

    private volatile LanternAuthCachePool lanternAuthCachePool = new LanternAuthCachePool();


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
            // 如果免认证呢？TODO
            return authenticationServiceResult.setSuccess(false);
        }

        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        List<Long> rolesIds = lanternAuthCachePool.getUserRoleMap().get(userInfo.getUiId());
        if (Objects.isNull(rolesIds)) {
            return authenticationServiceResult.setSuccess(false);
        }
        for (Long roleId : rolesIds) {
            Role role = lanternAuthCachePool.getRoleMap().get(roleId);
            if (Objects.isNull(role)) {
                continue;
            }
            List<Long> resourceIds = lanternAuthCachePool.getRoleResourceMap().get(roleId);
            if (Objects.isNull(resourceIds)) {
                continue;
            }
            for (Long resourceId : resourceIds) {
                Resources resources = lanternAuthCachePool.getResourceMap().get(resourceId);
                if (Objects.isNull(resources)) {
                    continue;
                }
                //TODO resource是什么
                if (Objects.equals(resources.getResourceId().toString(), resource)) {
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

    public Integer updateRoleResourceFull(Map<Long, List<Resources>> map){
        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        Map<Long, Resources> resourceMap = new HashMap<>(lanternAuthCachePool.getResourceMap());
        Map<Long, List<Long>> roleResourceMap = new HashMap<>(lanternAuthCachePool.getRoleResourceMap());
        for (Map.Entry<Long, List<Resources>> entry : map.entrySet()) {
            Long roleId = entry.getKey();
            List<Resources> resources = entry.getValue();
            List<Long> resourceIds = new ArrayList<>(resources.size());
            for (Resources resource : resources) {
                resourceMap.put(resource.getResourceId(),resource);
                resourceIds.add(resource.getResourceId());
            }
            roleResourceMap.put(roleId,resourceIds);
        }
        lanternAuthCachePool.setResourceMap(resourceMap);
        lanternAuthCachePool.setRoleResourceMap(roleResourceMap);
        return map.size();
    }

    public Integer updateUserRoleFull(Map<Long, List<Role>> map){
        LanternAuthCachePool lanternAuthCachePool = this.lanternAuthCachePool;
        Map<Long, Role> roleMap = new HashMap<>(lanternAuthCachePool.getRoleMap());
        Map<Long, List<Long>> userRoleMap = new HashMap<>(lanternAuthCachePool.getUserRoleMap());
        for (Map.Entry<Long, List<Role>> entry : map.entrySet()) {
            Long userId = entry.getKey();
            List<Role> roles = entry.getValue();
            List<Long> roleIds = new ArrayList<>(roles.size());
            for (Role role : roles) {
                roleMap.put(role.getRoleId(),role);
                roleIds.add(role.getRoleId());
            }
            userRoleMap.put(userId,roleIds);
        }
        lanternAuthCachePool.setRoleMap(roleMap);
        lanternAuthCachePool.setUserRoleMap(userRoleMap);
        return map.size();
    }

}
