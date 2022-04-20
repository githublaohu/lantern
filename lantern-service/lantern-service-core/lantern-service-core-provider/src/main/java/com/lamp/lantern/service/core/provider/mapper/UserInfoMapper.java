package com.lamp.lantern.service.core.provider.mapper;



import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserInfoMapper {

    static String TABLE_SQL = "user_info";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String UPDATE_SQL = "update " + TABLE_SQL + " set ";

    static String SELECT_SQL  = "select * from " + TABLE_SQL + " where ";

    @Insert({INSERT_SQL,
            "(ui_name,ui_nickname,ui_idcard,ui_phone,ui_email,ui_head_portrait,ui_lack_flag,ui_sex,ui_age,ui_birth,ui_address,ui_password,ui_salt,ui_salt_password,ui_token,ui_login_address,ui_login_time,ui_exit_time,ui_first_login,tri_id,ui_status)values",
            "(#{uiName},#{uiNickname},#{uiIdcard},#{uiPhone},#{uiEmail},#{uiHeadPortrait},#{uiLackFlag},#{uiSex},#{uiAge},#{uiBirth},#{uiAddress},#{uiPassword},#{uiSalt},#{uiSaltPassword},#{uiToken},#{uiLoginAddress},#{uiLoginTime},#{uiExitTime},#{uiFirstLogin},#{triId},#{uiStatus})"
    })
    public Integer insertUserInfoEntity(UserInfoEntity userInfoEntity);


    @Select({SELECT_SQL,
            "ui_name = #{uiName} or ui_phone = #{uiPhone} or ui_email = #{uiEmail}"
    })
    public UserInfoEntity queryUserInfoByLogin(UserInfoEntity userInfoEntity);

    @Select({SELECT_SQL,
            "ui_phone = #{uiPhone}"})
    public UserInfoEntity queryUserInfoByUiPhone(UserInfoEntity userInfoEntity);


    // 后端程序中对需要更改的字段进行更改 然后全部更新
    public Integer updateUserInfo(UserInfoEntity userInfoEntity);

    // 查询曾经登录过的Ip
    public List<String> queryLoginedIpByUiID(UserInfoEntity userInfoEntity);


}
