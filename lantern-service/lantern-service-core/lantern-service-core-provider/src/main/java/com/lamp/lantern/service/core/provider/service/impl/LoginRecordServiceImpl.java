package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.plugins.api.mode.LoginRecord;
import com.lamp.lantern.plugins.api.enums.LoginPatternEnum;
import com.lamp.lantern.plugins.api.enums.LoginStatusEnum;
import com.lamp.lantern.plugins.api.enums.SystemEnum;
import com.lamp.lantern.plugins.api.enums.TerminalEnum;
import com.lamp.lantern.service.core.provider.mapper.LoginRecordMapper;
import com.lamp.lantern.service.core.service.LoginRecordService;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;


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

//    @PostConstruct
//    public void test(){
//        LoginRecordEntity loginRecordEntity = new LoginRecordEntity();
//        loginRecordEntity.setUlId(1l);
//        loginRecordEntity.setUlSessionId("123");
//        loginRecordEntity.setUlLoginTime(LocalDateTime.now());
//        loginRecordEntity.setUlLoginAddress("Changsha");
//        loginRecordEntity.setUlLoginIp("211.1.1.1");
//        loginRecordEntity.setUlLoginSystem(SystemEnum.IOS);
//        loginRecordEntity.setUlLoginWay(LoginPatternEnum.PARTY_ACCOUNT);
//        loginRecordEntity.setUlLoginTerminal(TerminalEnum.MbBrowser);
//        loginRecordEntity.setTriId(1);
//        loginRecordEntity.setUlLoginStatus(LoginStatusEnum.SUCCESS);
//        this.insertLoginRecord(loginRecordEntity);
//    }

    @Override
    public Integer insertLoginRecord(LoginRecordEntity loginRecordEntity) {
       return loginRecordEntityMapper.insertLoginRecord(loginRecordEntity);
    }

    @Override
    public Integer insertLoginRecords(List<LoginRecordEntity> loginRecordEntities) {
        loginRecordEntities.forEach(loginRecordEntity -> {
            loginRecordEntityMapper.insertLoginRecord(loginRecordEntity);
        });
        return 1;
    }

    @Override
    public List<LoginRecord> checkLoginRecordByUserId(UserInfoEntity userInfoEntity) {
        return loginRecordEntityMapper.checkLoginRecordEntityByUserId(userInfoEntity);}

    @Override
    public List<LoginRecord> getAllLoginRecords() {
        List<LoginRecord> result = loginRecordEntityMapper.getAllLoginRecordEntity();
        return result;}

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
