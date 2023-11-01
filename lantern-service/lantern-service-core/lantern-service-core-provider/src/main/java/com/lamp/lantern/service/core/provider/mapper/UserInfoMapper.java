package com.lamp.lantern.service.core.provider.mapper;


import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserInfoMapper {

    static String TABLE_SQL = "user_info";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String SELECT_SQL = "select * from " + TABLE_SQL + " where ";

    static String UPDATE_SQL = "update " + TABLE_SQL + "set ";

@Insert({
        "<script>",
        "insert into user_info ",
        "(ui_name, ",
        "<if test='#{uiNickname} != null'>ui_nickname,</if>",
        "<if test='#{uiIdcard} != null'>ui_idcard,</if>",
        "<if test='#{uiPhone} != null'>ui_phone,</if>",
        "<if test='#{uiEmail} != null'>ui_email,</if>",
        "<if test='#{uiHeadPortrait} != null'>ui_head_portrait,</if>",
        "<if test='#{uiLackFlag} != null'>ui_lack_flag,</if>",
        "<if test='#{uiSex} != null'>ui_sex,</if>",
        "<if test='#{uiBirth} != null'>ui_birth,</if>",
        "<if test='#{uiAddress} != null'>ui_address,</if>",
        "<if test='#{uiLoginAddress} != null'>ui_login_address,</if>",
        "<if test='#{uiFirstLogin} != null'>ui_first_login,</if>",
        "<if test='#{uiStatus} != null'>ui_status,</if>",
        "<if test='#{allowLogin} != null'>allow_login,</if>",
        "ui_salt, ui_salt_password",
        ") values (",
        "#{uiName},",
        "<if test='#{uiNickname} != null'>#{uiNickname},</if>",
        "<if test='#{uiIdcard} != null'>#{uiIdcard},</if>",
        "<if test='#{uiPhone} != null'>#{uiPhone},</if>",
        "<if test='#{uiEmail} != null'>#{uiEmail},</if>",
        "<if test='#{uiHeadPortrait} != null'>#{uiHeadPortrait},</if>",
        "<if test='#{uiLackFlag} != null'>#{uiLackFlag},</if>",
        "<if test='#{uiSex} != null'>#{uiSex},</if>",
        "<if test='#{uiBirth} != null'>#{uiBirth},</if>",
        "<if test='#{uiAddress} != null'>#{uiAddress},</if>",
        "<if test='#{uiLoginAddress} != null'>#{uiLoginAddress},</if>",
        "<if test='#{uiFirstLogin} != null'>#{uiFirstLogin},</if>",
        "<if test='#{uiStatus} != null'>#{uiStatus},</if>",
        "<if test='#{allowLogin} != null'>#{allowLogin},</if>",
        "#{uiSalt}, #{uiSaltPassword}",
        ")",
        "</script>"
        })
    //TODO:有问题, 不能检测到null
public Integer registerUserInfoEntity(UserInfo userInfoEntity);

    @Results(
            id = "userMap",
            value = {
                    @Result(column = "ui_id", property = "uiId"), @Result(column = "ui_name", property = "uiName"), @Result(column = "ui_idcard", property = "uiIdcard"),
                    @Result(column = "ui_phone", property = "uiPhone"), @Result(column = "ui_email", property = "uiEmail"), @Result(column = "ui_head_portrait", property = "uiHeadPortrait"),
                    @Result(column = "ui_lack_flag", property = "uiLackFlag"), @Result(column = "ui_sex", property = "uiSex"), @Result(column = "ui_age", property = "uiAge"), @Result(column = "ui_birth", property = "uiBirth"),
                    @Result(column = "ui_address", property = "uiAddress"), @Result(column = "ui_password", property = "uiPassword"), @Result(column = "ui_salt", property = "uiSalt"), @Result(column = "ui_salt_password", property = "uiSaltPassword"), @Result(column = "ui_token", property = "uiToken"),
                    @Result(column = "ui_login_address", property = "uiLoginAddress"), @Result(column = "ui_login_time", property = "uiLoginTime"), @Result(column = "ui_exit_time", property = "uiExitTime"), @Result(column = "ui_first_login", property = "uiFirstLogin"),
                    @Result(column = "ui_status", property = "uiStatus"), @Result(column = "allow_login", property = "allowLogin")
            }
    )
    @Select({SELECT_SQL,
            "ui_name = #{uiName}"
    })
    public UserInfoEntity checkUserExistByUserName(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_email = #{uiEmail}"

    })
    public UserInfoEntity checkUserExistByEmail(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_phone = #{uiPhone}"
    })
    public UserInfoEntity checkUserExistByPhone(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_name = 'jaycase'"
    })//TODO ?
    public UserInfoEntity queryUserByUserName(UserInfo userInfoEntity);

    @Select("select ui_id from `user_info` where ui_name = 'jaycase';")
    public Integer quertUserById();

    @ResultMap("userMap")
    @Select("select * from `user_info` where ui_name = 'jaycase'")
    public UserInfoEntity testQuery();

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_id = #{uiId}"
    })
    public UserInfoEntity checkUserByUserId(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_name = #{uiName} or ui_phone = #{uiPhone} or ui_email = #{uiEmail}"
    })
    public UserInfoEntity checkUserByUserIdOrPhoneOrEmail(UserInfo userInfoEntity);

    @Update({UPDATE_SQL,
            "allow_login = #{allowLogin} where ui_id = #{uiId}"
    })
    public Integer updateUserAllowLoginField(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
        "ui_idcard = #{uiIdcard}"
    })
    public UserInfoEntity checkUserExistByIdcard(UserInfo userInfoEntity);

    @Update("update user_info set ui_is_delete = 1 where ui_id = #{uiId}")
    Integer deleteUser(UserInfoEntity userInfoEntity);

    @Update({
            "<script>",
            "update user_info set ui_is_delete = 1 ",
            "where ui_id in ",
            "<foreach collection='userInfoEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.uiId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteUsers(List<UserInfoEntity> userInfoEntities);

    @Select("select * from user_info")
    List<UserInfo> getAllUserInfos();

    @ResultMap("userMap")
    @Options(useGeneratedKeys = true, keyProperty = "uiId", keyColumn = "ui_id")
    @Insert("insert into user_info (ui_name) values (#{uiName})")
    Integer registerThirdLoginUser(UserInfoEntity userInfo);
}
