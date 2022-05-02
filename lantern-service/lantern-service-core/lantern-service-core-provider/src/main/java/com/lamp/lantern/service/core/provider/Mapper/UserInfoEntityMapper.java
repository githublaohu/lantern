package com.lamp.lantern.service.core.provider.Mapper;


import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoEntityMapper {

    static String TABLE_SQL = "user_info";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String SELECT_SQL = "select * from " + TABLE_SQL + " where ";

    static String UPDATE_SQL = "update " + TABLE_SQL + "set ";

    @Insert({INSERT_SQL,
            "(ui_name,ui_nickname,ui_idcard,ui_phone,ui_email,ui_head_portrait,ui_lack_flag,ui_sex,ui_age,ui_birth,ui_address,ui_password,ui_salt,ui_salt_password,ui_token,ui_login_address,ui_login_time,ui_exit_time,ui_first_login,tri_id,ui_status)values",
            "(#{uiName},#{uiNickname},#{uiIdcard},#{uiPhone},#{uiEmail},#{uiHeadPortrait},#{uiLackFlag},#{uiSex},#{uiAge},#{uiBirth},#{uiAddress},#{uiPassword},#{uiSalt},#{uiSaltPassword},#{uiToken},#{uiLoginAddress},#{uiLoginTime},#{uiExitTime},#{uiFirstLogin},#{triId},#{uiStatus})"
    })
    public Integer registerUserInfoEntity(UserInfoEntity userInfoEntity);

    @Results(
            id = "userMap",
            value = {
                    @Result(column = "ui_id", property = "uiId"), @Result(column = "ui_name", property = "uiName"), @Result(column = "ui_idcard", property = "uiIdcard"),
                    @Result(column = "ui_phone", property = "uiPhone"), @Result(column = "ui_email", property = "uiEmail"), @Result(column = "ui_head_portrait", property = "uiHeadPortrait"),
                    @Result(column = "ui_lack_flag", property = "uiLackFlag"), @Result(column = "ui_sex", property = "uiSex"), @Result(column = "ui_age", property = "uiAge"), @Result(column = "ui_birth", property = "uiBirth"),
                    @Result(column = "ui_address", property = "uiAddress"), @Result(column = "ui_password", property = "uiPassword"), @Result(column = "ui_salt", property = "uiSalt"), @Result(column = "ui_salt_password", property = "uiSaltPassword"), @Result(column = "ui_token", property = "uiToken"),
                    @Result(column = "ui_login_address", property = "uiLoginAddress"), @Result(column = "ui_login_time", property = "uiLoginTime"), @Result(column = "ui_exit_time", property = "uiExitTime"), @Result(column = "ui_first_login", property = "uiFirstLogin"), @Result(column = "tri_id", property = "triId"),
                    @Result(column = "ui_status", property = "uiStatus"), @Result(column = "allow_login", property = "allowLogin")
            }
    )
    @Select({SELECT_SQL,
            "ui_name = #{uiName}"
    })
    public UserInfoEntity checkUserExistByUserName(UserInfoEntity userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_email = #{uiEmail}"

    })
    public UserInfoEntity checkUserExistByEmail(UserInfoEntity userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_phone = #{uiPhone}"
    })
    public UserInfoEntity checkUserExistByPhone(UserInfoEntity userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_name = 'jaycase'"
    })
    public UserInfoEntity queryUserByUserName(UserInfoEntity userInfoEntity);

    @Select("select ui_id from user_info where ui_name = 'jaycase';")
    public Integer quertUserById();

    @ResultMap("userMap")
    @Select("select * from user_info where ui_name = 'jaycase'")
    public UserInfoEntity testQuery();

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_id = #{uiId}"
    })
    public UserInfoEntity checkUserByUserId(UserInfoEntity userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_name = #{uiName} or ui_phone = #{uiPhone} or ui_email = #{uiEmail}"
    })
    public UserInfoEntity checkUserByUserIdOrPhoneOrEmail(UserInfoEntity userInfoEntity);

    @Update({UPDATE_SQL,
            "allow_login = #{allowLogin} where ui_id = #{uiId}"
    })
    public Integer updateUserAllowLoginField(UserInfoEntity userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
        "ui_idcard = #{uiIdcard}"
    })
    public UserInfoEntity checkUserExistByIdcard(UserInfoEntity userInfoEntity);
}
