package com.lamp.lantern.service.core.service;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import java.util.List;

public interface LoginRecordEntityService {

    public Integer insertLoginRecord(LoginRecordEntity loginRecordEntity);

    public List<LoginRecordEntity> checkLoginRecordEntitysByUserId(UserInfoEntity userInfoEntity);

    public Integer updateLoginRecordEntityExitTimeField(LoginRecordEntity loginRecordEntity);

    public Integer updateLoginRecordEntityQuitWayField(LoginRecordEntity loginRecordEntity);

}
