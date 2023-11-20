package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.auth.config.RedisCacheConfig;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import io.lettuce.core.RedisClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheAuthenticationServiceTest {
    private CacheAuthenticationService cacheAuthenticationService = null;

    private AuthenticationData authenticationData = new AuthenticationData();

    @Before
    public void init() {
        RedisCacheConfig redisCacheConfig = new RedisCacheConfig();
        redisCacheConfig.setRedisCacheUrl("redis://127.0.0.1:6379/0");
        redisCacheConfig.setRedisCacheSystemName("testCache");

        cacheAuthenticationService = new CacheAuthenticationService(redisCacheConfig);

        HashMap<String, UserInfo> map = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        map.put("userInfo", userInfo);
        map.put("userInfo1", userInfo);
        cacheAuthenticationService.updateTokenUser(map);

        HashMap<Long, List<Role>> userRoleMap = new HashMap<>();
        Role role = new Role();
        role.setRoleId(1L);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        userRoleMap.put(1L, roles);

        cacheAuthenticationService.updateUserRole(userRoleMap);

        HashMap<Long, List<Resources>> roleResourceMap = new HashMap<>();
        Resources resources = new Resources();
        resources.setResourceId(1L);
        List<Resources> resourcesList = new ArrayList<>();
        resourcesList.add(resources);
        roleResourceMap.put(1L, resourcesList);
        cacheAuthenticationService.updateRoleResource(roleResourceMap);

    }

    @Test
    public void getUserInfoTest() {
        authenticationData.setToken("userInfo");
        AuthenticationServiceResult result = cacheAuthenticationService.getUserInfo(authenticationData);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(1L, result.getUserInfo().getUiId().longValue());
        authenticationData.setToken("userInfo2");
        Assert.assertFalse(cacheAuthenticationService.getUserInfo(authenticationData).isSuccess());
    }

    @Test
    public void authenticationTest() {
        UserInfo user1 = new UserInfo();
        user1.setUiId(1L);
        authenticationData.setUserInfo(user1);
        authenticationData.setResource("1");
        AuthenticationServiceResult result =  cacheAuthenticationService.authentication(authenticationData);
        Assert.assertTrue(result.isSuccess());
        authenticationData.setResource("2");
        Assert.assertFalse(cacheAuthenticationService.authentication(authenticationData).isSuccess());
    }
}
