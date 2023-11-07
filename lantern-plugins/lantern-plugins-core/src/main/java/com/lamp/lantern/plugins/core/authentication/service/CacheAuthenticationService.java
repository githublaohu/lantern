package com.lamp.lantern.plugins.core.authentication.service;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author laohu
 */
public class CacheAuthenticationService implements AuthenticationService {

    @Setter
    protected StatefulRedisConnection<String, String> connection;

    static String tokenPrefix = "CacheAuthenticationService:tokenUser:";
    static String userPrefix = "CacheAuthenticationService:userRole:";
    static String rolePrefix = "CacheAuthenticationService:roleResource:";

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();
        String token = authData.getToken();
        if(Objects.isNull(token)){
            return authenticationServiceResult.setSuccess(false);
        }
        String userInfoStr = connection.sync().get(tokenPrefix+token);
        if (Objects.isNull(userInfoStr)) {
            return authenticationServiceResult.setSuccess(false);
        }
        UserInfo userInfo = JSONObject.parseObject(userInfoStr, UserInfo.class);
        authenticationServiceResult.setSuccess(true);
        authenticationServiceResult.setUserInfo(userInfo);
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
        String userId = userInfo.getUiId().toString();

        String rolesStr = connection.sync().get(userPrefix+userId);
        if (Objects.isNull(rolesStr)) {
            return authenticationServiceResult.setSuccess(false);
        }
        Role[] roles = JSONObject.parseObject(rolesStr, Role[].class);
        for (Role role : roles) {
            String resourcesStr = connection.sync().get(rolePrefix+role.getRoleId().toString());
            Resources[] resources = JSONObject.parseObject(resourcesStr, Resources[].class);
            for (Resources resources1 : resources) {
                if (Objects.equals(resources1.getResourceId().toString(), resource)) {
                    return authenticationServiceResult.setSuccess(true);
                }
            }
        }
        return authenticationServiceResult.setSuccess(false);
    }

    public void updateTokenUser(Map<String, UserInfo> map){
        for (Map.Entry<String, UserInfo> entry : map.entrySet()) {
            String token = entry.getKey();
            UserInfo userInfo = entry.getValue();
            String userInfoStr = JSONObject.toJSONString(userInfo);
            connection.async().set(tokenPrefix+token, userInfoStr);
        }
    }
    public void updateUserRole(Map<Long, List<Role>> map){
        for (Map.Entry<Long, List<Role>> entry : map.entrySet()) {
            Long userId = entry.getKey();
            List<Role> roles = entry.getValue();
            String rolesStr = JSONObject.toJSONString(roles);
            connection.async().set(userPrefix+userId.toString(), rolesStr);
        }
    }
    public void updateRoleResource(Map<Long, List<Resources>> map){
        for (Map.Entry<Long, List<Resources>> entry : map.entrySet()) {
            Long roleId = entry.getKey();
            List<Resources> resources = entry.getValue();
            String resourcesStr = JSONObject.toJSONString(resources);
            connection.async().set(rolePrefix+roleId.toString(), resourcesStr);
        }
    }

}

/*
 * Redis结构
 * Token  -> UserInfo(JSON);
 * UserId -> Role[](JSON);
 * RoleId -> Resource[](JSON);
 */