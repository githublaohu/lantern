package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.LoginRecordMapper;
import com.lamp.lantern.service.core.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LoginRecordServiceImpl implements LoginRecordService {

    @Autowired
    private LoginRecordMapper loginRecordMapper;

    @Override
    public List<LoginRecordEntity> queryLoginRecordByUiId(UserInfoEntity userInfoEntity) {
        return null;
    }

    @Override
    public Integer insertLoginRecord(LoginRecordEntity userLoginRecord) {
        return null;
    }

    @Override
    public Integer updateLoginRecord(LoginRecordEntity userLoginRecord) {
        return null;
    }

    @Override
    public Integer queryUserValidLoginRecordToday(UserInfoEntity userInfoEntity){
        return 0;
    }
}
