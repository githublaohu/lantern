package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationRpcService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.ResourcesMapper;
import com.lamp.lantern.service.core.provider.mapper.RoleMapper;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import org.apache.catalina.User;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AuthenticationRpcServiceImpl implements AuthenticationRpcService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByToken(authData.getToken());
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();
        if (Objects.isNull(userInfo)) {
            return authenticationServiceResult.setSuccess(false);
        }
        authenticationServiceResult.setUserInfo(userInfo);
        return authenticationServiceResult.setSuccess(true);
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        String resource = authData.getResource();
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();

        List<Role> roleEntities = roleMapper.getAllValidRoleByUserId(authData.getUserInfo());
        List<Resources> resources = resourcesMapper.selectValidResourcesByRoleIds(roleEntities);
        for (Resources resources1 : resources) {
            //这里判断了资源的id，但是进来的资源是什么?
            if (resources1.getResourceId().toString().equals(resource)) {
                return authenticationServiceResult.setSuccess(true);
            }
        }
        return authenticationServiceResult.setSuccess(false);
    }


}
