package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicy;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicy.Mode;
import com.lamp.lantern.plugins.api.auth.AuthenticationPolicyService;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.provider.mapper.ResourcesMapper;
import com.lamp.lantern.service.core.provider.mapper.RoleMapper;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author hahaha
 */
@Service("DBAuthentication")
public class DBAuthenticationServiceImpl implements AuthenticationService, AuthenticationPolicyService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        AuthenticationServiceResult result = new AuthenticationServiceResult();
        if (!StringUtils.hasText(authData.getToken())) {
            return result.setSuccess(false);
        }
        UserInfo userInfo;
        if (authData.getSystemId() == null) {
            userInfo = userInfoMapper.selectUserInfoByToken(authData.getToken());
        } else {
            List<UserInfo> users = userInfoMapper.selectActiveUsersByToken(authData.getSystemId(), authData.getToken());
            userInfo = users.size() == 1 ? users.get(0) : null;
        }
        result.setUserInfo(userInfo);
        if (userInfo == null) {
            result.setErrorMessage("无法通过 token 查询到用户");
        }
        return result.setSuccess(userInfo != null);
    }

    @Override
    public AuthenticationPolicy findPolicy(Long systemId, String method, String route) {
        List<Resources> resources = resourcesMapper.findActiveHttpPolicies(systemId, method, route);
        if (resources.isEmpty()) {
            return null;
        }
        if (resources.size() != 1) {
            throw new IllegalStateException("Conflicting Lantern HTTP resource policies");
        }
        Resources resource = resources.get(0);
        return new AuthenticationPolicy(resource.getResourceId(), Mode.valueOf(resource.getResourceConditions()));
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();
        if (authData.getResourceId() == null && !StringUtils.hasText(authData.getResource())) {
            return authenticationServiceResult.setSuccess(false);
        }
        if (authData.getUserId() == null) {
            AuthenticationServiceResult authenticationServiceResult1 = this.getUserInfo(authData);
            if (!authenticationServiceResult1.isSuccess()) {
                return authenticationServiceResult1;
            }
            authenticationServiceResult.setUserInfo(authenticationServiceResult1.getUserInfo());
            authData.setUserId(authenticationServiceResult1.getUserInfo().getUiId());
        }
        return authenticationServiceResult.setSuccess(resourcesMapper.hasResourcePermission(authData));
    }


}
