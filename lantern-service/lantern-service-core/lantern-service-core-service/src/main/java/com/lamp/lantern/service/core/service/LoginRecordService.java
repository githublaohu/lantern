package com.lamp.lantern.service.core.service;

import java.util.List;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface LoginRecordService {

    public Integer insertLoginRecord(LoginRecordEntity loginRecordEntity);

    public List<LoginRecordEntity> checkLoginRecordEntitysByUserId(UserInfoEntity userInfoEntity);

    public Integer updateLoginRecordEntityExitTimeField(LoginRecordEntity loginRecordEntity);

    public Integer updateLoginRecordEntityQuitWayField(LoginRecordEntity loginRecordEntity);

}
