package com.lamp.lantern.service.core.provider;

import org.apache.ibatis.annotations.Select;

import com.lamp.lantern.service.core.entity.UserInfoEntity;

public class TestApp {

    static String TABLE_SQL = "user_info";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String SELECT_SQL = "select * from " + TABLE_SQL + " where ";


    public interface TestQuery{

        @Select("select * from user_info where ui_name = #{uiName}")
        UserInfoEntity queryUserByUserName(UserInfoEntity userInfoEntity);

    }

    public static void main(String[] args) {

        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUiName("jaycase");





    }
}
