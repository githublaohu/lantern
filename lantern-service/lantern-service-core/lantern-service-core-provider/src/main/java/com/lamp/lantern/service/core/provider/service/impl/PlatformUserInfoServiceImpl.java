package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.PlatformUserInfoEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.PlatformUserInfoMapper;
import com.lamp.lantern.service.core.service.PlatformUserInfoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlatformUserInfoServiceImpl implements PlatformUserInfoService {

    @Autowired
    private PlatformUserInfoMapper platformUserInfoMapper;
    @Override
    public Integer registerPlatformUserInfo(PlatformUserInfoEntity platformUserInfoEntity) {
        return platformUserInfoMapper.insertPlatformUserInfo(platformUserInfoEntity);
    }

    @Override
    public UserInfo checkUserByUserIdAndTriId(PlatformUserInfoEntity platformUserInfoEntity) {
        return platformUserInfoMapper.checkPlatformUserByUserIdAndTriId(platformUserInfoEntity);
    }

    @Override
    public UserInfoEntity checkUserByTriIdAndAuthchannel(PlatformUserInfoEntity platformUserInfoEntity) {
//        return  platformUserInfoMapper.checkUserByTriIdAndAuthchannel(platformUserInfoEntity);
        return null;
    }


}
