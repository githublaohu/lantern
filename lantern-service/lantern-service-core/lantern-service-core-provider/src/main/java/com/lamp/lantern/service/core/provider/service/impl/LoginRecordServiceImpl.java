package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.enums.LoginPatternEnum;
import com.lamp.lantern.plugins.api.enums.LoginStatusEnum;
import com.lamp.lantern.plugins.api.enums.SystemEnum;
import com.lamp.lantern.plugins.api.enums.TerminalEnum;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.plugins.api.mode.LoginRecord;
import com.lamp.lantern.service.core.provider.mapper.LoginRecordMapper;
import com.lamp.lantern.service.core.service.LoginRecordService;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resources;


@Service
@Transactional
public class LoginRecordServiceImpl implements LoginRecordService {


    @Autowired
    private LoginRecordMapper loginRecordEntityMapper;

    @Override
    public Integer insertLoginRecord(LoginRecordEntity loginRecordEntity) {
        loginRecordEntityMapper.insertLoginRecord(loginRecordEntity);
        return loginRecordEntity.getUlId().intValue();
    }

    @Override
    public List<LoginRecordEntity> checkLoginRecordByUserId(UserInfoEntity userInfoEntity) {
        return loginRecordEntityMapper.checkLoginRecordEntityByUserId(userInfoEntity);
    }

    @Override
    public List<LoginRecordEntity> getAllLoginRecords() {
        return loginRecordEntityMapper.getAllLoginRecordEntity();
    }

    @Override
    public Integer updateLoginRecordExitTimeField(LoginRecordEntity loginRecordEntity) {
        return loginRecordEntityMapper.updateLoginRecordEntityExitTimeField(loginRecordEntity);
    }

    @Override
    public Integer updateLoginRecordQuitWayField(LoginRecordEntity loginRecordEntity) {
        String config_file = "classpath:/application.yml";

        InputStream inputStream = Resources.class.getResourceAsStream(config_file);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer rows = sqlSession.getMapper(LoginRecordMapper.class).insertLoginRecord(loginRecordEntity);
        return rows;
    }
}
