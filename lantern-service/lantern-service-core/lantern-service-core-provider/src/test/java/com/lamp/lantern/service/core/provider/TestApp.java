package com.lamp.lantern.service.core.provider;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.Mapper.UserInfoEntityMapper;
import org.apache.ibatis.annotations.Select;

public class TestApp {

    static String TABLE_SQL = "user_info";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String SELECT_SQL = "select * from " + TABLE_SQL + " where ";


    private interface TestQuery{

        @Select("select * from user_info where ui_name = #{uiName}")
        UserInfoEntity queryUserByUserName(UserInfoEntity userInfoEntity);

    }

    public static void main(String[] args) {

        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUiName("jaycase");





    }
}
