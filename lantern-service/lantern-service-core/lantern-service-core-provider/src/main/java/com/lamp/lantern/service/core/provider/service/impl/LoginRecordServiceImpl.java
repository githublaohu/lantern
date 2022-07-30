package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.LoginRecordMapper;
import com.lamp.lantern.service.core.service.LoginRecordService;

import java.io.InputStream;
import java.util.List;


import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resources;


@Service
@Transactional
public class LoginRecordServiceImpl implements LoginRecordService {


    @Autowired(required = true)
    private LoginRecordMapper loginRecordEntityMapper;

    @Override
    public Integer insertLoginRecord(LoginRecordEntity loginRecordEntity) {
        loginRecordEntityMapper.insertLoginRecord(loginRecordEntity);
        System.out.println("getUlId " + loginRecordEntity.getUlId());
        return 1;

    }

    @Override
    public List<LoginRecordEntity> checkLoginRecordEntitysByUserId(UserInfoEntity userInfoEntity) {
        return loginRecordEntityMapper.checkLoginRecordEntitysByUserId(userInfoEntity);
    }

    @Override
    public Integer updateLoginRecordEntityExitTimeField(LoginRecordEntity loginRecordEntity) {
        return loginRecordEntityMapper.updateLoginRecordEntityExitTimeField(loginRecordEntity);
    }

    @Override
    public Integer updateLoginRecordEntityQuitWayField(LoginRecordEntity loginRecordEntity) {
        String config_file = "classpath:/application.yml";

        InputStream inputStream = Resources.class.getResourceAsStream(config_file);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer rows = sqlSession.getMapper(LoginRecordMapper.class).insertLoginRecord(loginRecordEntity);
        return rows;
    }
}
