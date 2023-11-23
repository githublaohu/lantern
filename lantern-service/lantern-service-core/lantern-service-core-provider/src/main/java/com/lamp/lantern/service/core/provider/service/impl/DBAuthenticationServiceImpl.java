package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.provider.mapper.ResourcesMapper;
import com.lamp.lantern.service.core.provider.mapper.RoleMapper;
import com.lamp.lantern.service.core.provider.mapper.UserInfoMapper;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hahaha
 */
@Service("DBAuthentication")
@Transactional
public class DBAuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByToken(authData.getToken());
        //
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();
        authenticationServiceResult.setUserInfo(userInfo);
        if (Objects.isNull(userInfo)) {
            String message = String.format("无法通过 token 查询用到用户，token ID is %s", authData.getToken());
            authenticationServiceResult.setErrorMessage(message);
            authenticationServiceResult.setSuccess(false);

        } else {
            authenticationServiceResult.setSuccess(true);
        }
        return authenticationServiceResult;
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult();
        if (Objects.isNull(authData.getUserId())) {
            AuthenticationServiceResult authenticationServiceResult1 = this.getUserInfo(authData);
            if (!authenticationServiceResult1.isSuccess()) {
                return authenticationServiceResult1;
            }
            authenticationServiceResult.setUserInfo(authenticationServiceResult1.getUserInfo());
            authData.setUserId(authenticationServiceResult1.getUserInfo().getUiId());
        }
        Resources resources = resourcesMapper.authentication(authData);
        return authenticationServiceResult.setSuccess(Objects.nonNull(resources));
    }


}
