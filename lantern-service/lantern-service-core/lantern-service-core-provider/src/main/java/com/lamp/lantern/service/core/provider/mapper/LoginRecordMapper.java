package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;

import java.util.List;


@Mapper
public interface LoginRecordMapper {

    static String TABLE_SQL = "login_record";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String UPDATE_SQL = "update " + TABLE_SQL + " set ";

    static String SELECT_SQL  = "select * from " + TABLE_SQL + " where ";

    public List<LoginRecordEntity> queryLoginRecordByUiId(UserInfoEntity userInfoEntity);

    public Integer insertLoginRecord(LoginRecordEntity userLoginRecord);

    public Integer updateLoginRecord(LoginRecordEntity userLoginRecord);

    public List<LoginRecordEntity> queryUserValidLoginRecordToday(UserInfoEntity userInfoEntity);



}
