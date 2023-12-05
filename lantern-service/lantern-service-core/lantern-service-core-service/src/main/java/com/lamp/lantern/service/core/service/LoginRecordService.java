package com.lamp.lantern.service.core.service;

import java.util.List;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.plugins.api.mode.LoginRecord;

public interface LoginRecordService {

    public Integer insertLoginRecord(LoginRecordEntity loginRecordEntity);

    public List<Integer> insertLoginRecords(List<LoginRecordEntity> loginRecordEntities);

    public List<LoginRecord> checkLoginRecordByUserId(UserInfoEntity userInfoEntity);

    public List<LoginRecord> getAllLoginRecords();

    public Integer updateLoginRecordExitTimeField(LoginRecordEntity loginRecordEntity);

    public Integer updateLoginRecordQuitWayField(LoginRecordEntity loginRecordEntity);

}
