package com.lamp.lantern.service.core.service;

import java.util.List;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface LoginRecordService {

    public Integer insertLoginRecord(LoginRecordEntity loginRecordEntity);

    public List<LoginRecordEntity> checkLoginRecordByUserId(UserInfoEntity userInfoEntity);

    public List<LoginRecordEntity> getAllLoginRecords();

    public Integer updateLoginRecordExitTimeField(LoginRecordEntity loginRecordEntity);

    public Integer updateLoginRecordQuitWayField(LoginRecordEntity loginRecordEntity);

}
