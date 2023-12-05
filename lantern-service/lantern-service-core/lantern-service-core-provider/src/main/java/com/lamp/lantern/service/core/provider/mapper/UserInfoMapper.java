package com.lamp.lantern.service.core.provider.mapper;


import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * create or replace table user_info
 * (
 *     ui_id             bigint unsigned auto_increment
 *     primary key,
 *     system_id         bigint                                                         not null comment '系统id',
 *     ui_name           varchar(36)                                                    not null comment '用户名，使用前缀索引',
 *     ui_nickname       varchar(16)                        default `ui_name`           not null comment '用户昵称',
 *     ui_id_card        varchar(36)                                                    not null comment '用户唯一标识符，使用前缀索引',
 *     ui_phone          varchar(36)                        default ''                  not null comment '用户联系方式，使用前缀索引',
 *     ui_email          varchar(36)                        default ''                  not null comment '用户邮箱，使用前缀索引',
 *     ui_head_portrait  varchar(32)                        default ''                  not null comment '用户图像,默认为用户id',
 *     ui_sex            enum ('MALE', 'FEMALE', 'UNKNOWN') default 'UNKNOWN'           not null comment '用户性别，可以在详细信息表',
 *     ui_birth          date                               default '1970-01-01'        not null comment '用户生日，可以在详细信息表',
 *     ui_address        varchar(32)                        default ''                  not null comment '用户家庭地址，可以在详细信息表',
 *     ui_salt           varchar(64)                        default ''                  not null comment '用户盐',
 *     ui_salt_password  varchar(64)                        default ''                  not null comment '用户盐密',
 *     ui_login_address  varchar(32)                        default ''                  not null comment '用户最近登录地址，应该在登录记录表里面',
 *     ui_login_time     datetime                           default current_timestamp() not null comment '用户最近登录时间，，应该在登录记录表里面',
 *     ui_exit_time      datetime                           default current_timestamp() not null comment '用户最近退出时间，，应该在登录记录表里面',
 *     ui_status         bigint                             default 0                   not null comment '用户状态。1 第一次登录。2：认证中',
 *     ui_token          varchar(128)                       default ''                  not null comment '用于简单系统，简单系统建立所有',
 *     ui_create_time    datetime                           default current_timestamp() not null comment ' 创建时间 ',
 *     ui_create_user_id bigint                                                         not null comment '创建人id',
 *     ui_update_time    datetime                           default current_timestamp() not null on update current_timestamp() comment ' 修改时间 ',
 *     ui_update_user_id bigint                                                         not null comment '修改人id',
 *     is_delete         int                                default 0                   not null comment '0未删除，1已删除'
 * );
 */
@Mapper
public interface UserInfoMapper {

    static String TABLE_SQL = "user_info";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String SELECT_SQL = "select * from " + TABLE_SQL + " where ";

    static String UPDATE_SQL = "update " + TABLE_SQL + "set ";

    @Options(useGeneratedKeys = true, keyProperty = "uiId", keyColumn = "ui_id")
    @Insert({
        "<script>",
        "insert into user_info",
        "(ui_name, ",
        "<if test='#{systemId} != null'>system_id,</if>",
        "<if test='#{uiNickname} != null'>ui_nickname,</if>",
        "<if test='#{uiIdCard} != null'>ui_id_card,</if>",
        "<if test='#{uiPhone} != null'>ui_phone,</if>",
        "<if test='#{uiEmail} != null'>ui_email,</if>",
        "<if test='#{uiHeadPortrait} != null'>ui_head_portrait,</if>",
        "<if test='#{uiSex} != null'>ui_sex,</if>",
        "<if test='#{uiBirth} != null'>ui_birth,</if>",
        "<if test='#{uiAddress} != null'>ui_address,</if>",
        "<if test='#{uiLoginAddress} != null'>ui_login_address,</if>",
        "<if test='#{uiFirstLogin} != null'>ui_first_login,</if>",
        "<if test='#{uiStatus} != null'>ui_status,</if>",
        "<if test='#{uiToken} != null'>ui_token,</if>",
        "<if test='#{allowLogin} != null'>allow_login,</if>",
        "ui_salt, ui_salt_password,ui_create_time,ui_create_user_id,ui_update_time,ui_update_user_id",
        ") values (",
        "#{uiName},",
        "<if test='#{systemId} != null'>#{systemId},</if>",
        "<if test='#{uiNickname} != null'>#{uiNickname},</if>",
        "<if test='#{uiIdCard} != null'>#{uiIdCard},</if>",
        "<if test='#{uiPhone} != null'>#{uiPhone},</if>",
        "<if test='#{uiEmail} != null'>#{uiEmail},</if>",
        "<if test='#{uiHeadPortrait} != null'>#{uiHeadPortrait},</if>",
        "<if test='#{uiSex} != null'>#{uiSex},</if>",
        "<if test='#{uiBirth} != null'>#{uiBirth},</if>",
        "<if test='#{uiAddress} != null'>#{uiAddress},</if>",
        "<if test='#{uiLoginAddress} != null'>#{uiLoginAddress},</if>",
        "<if test='#{uiFirstLogin} != null'>#{uiFirstLogin},</if>",
        "<if test='#{uiStatus} != null'>#{uiStatus},</if>",
        "<if test='#{uiToken} != null'>#{uiToken},</if>",
        "<if test='#{allowLogin} != null'>#{allowLogin},</if>",
        "#{uiSalt}, #{uiSaltPassword},now(),#{operatorId},now(),#{operatorId}",
        "</script>"
    })
        //TODO:有问题, 不能检测到null
         Integer registerUserInfoEntity(UserInfo userInfoEntity);

    @Results(
            id = "userMap",
            value = {
                    @Result(column = "ui_id", property = "uiId"),
                    @Result(column = "system_id", property = "systemId"),
                    @Result(column = "ui_name", property = "uiName"),
                    @Result(column = "ui_nickname", property = "uiNickname"),
                    @Result(column = "ui_id_card", property = "uiIdCard"),
                    @Result(column = "ui_phone", property = "uiPhone"),
                    @Result(column = "ui_email", property = "uiEmail"),
                    @Result(column = "ui_head_portrait", property = "uiHeadPortrait"),
                    @Result(column = "ui_sex", property = "uiSex"),
                    @Result(column = "ui_birth", property = "uiBirth"),
                    @Result(column = "ui_address", property = "uiAddress"),
                    @Result(column = "ui_login_address", property = "uiLoginAddress"),
                    @Result(column = "ui_login_time", property = "uiLoginTime"),
                    @Result(column = "ui_exit_time", property = "uiExitTime"),
                    @Result(column = "ui_first_login", property = "uiFirstLogin"),

            }
    )
    @Select({SELECT_SQL,
            "ui_name = #{uiName} and is_delete = 0"
    })
     UserInfoEntity checkUserExistByUserName(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_email = #{uiEmail} and is_delete = 0"

    })
     UserInfoEntity checkUserExistByEmail(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_phone = #{uiPhone} and is_delete = 0"
    })
     UserInfoEntity checkUserExistByPhone(UserInfo userInfoEntity);
    
    @ResultMap("userMap")
    @Select("select * from `user_info` where ui_name = 'jaycase'")
     UserInfoEntity testQuery();

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_id = #{uiId} and is_delete = 0"
    })
     UserInfoEntity checkUserByUserId(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
            "ui_name = #{uiName} or ui_phone = #{uiPhone} or ui_email = #{uiEmail} and is_delete = 0"
    })
     UserInfoEntity checkUserByUserIdOrPhoneOrEmail(UserInfo userInfoEntity);

    @Update({UPDATE_SQL,
            "allow_login = #{allowLogin} where ui_id = #{uiId}"
    })
     Integer updateUserAllowLoginField(UserInfo userInfoEntity);

    @ResultMap("userMap")
    @Select({SELECT_SQL,
        "ui_id_card = #{uiIdCard} and is_delete = 0"
    })
     UserInfoEntity checkUserExistByIdcard(UserInfo userInfoEntity);

    @Update("update user_info set is_delete = 1 where ui_id = #{uiId}")
    Integer deleteUser(UserInfoEntity userInfoEntity);

    @Update({
            "<script>",
            "update user_info set is_delete = 1 ",
            "where ui_id in ",
            "<foreach collection='userInfoEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.uiId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteUsers(List<UserInfoEntity> userInfoEntities);

    @Select("select * from user_info where is_delete = 0")
    List<UserInfo> getAllUserInfos();

    @ResultMap("userMap")
    @Options(useGeneratedKeys = true, keyProperty = "uiId", keyColumn = "ui_id")
    @Insert("insert into user_info (ui_name) values (#{uiName})")
    Integer registerThirdLoginUser(UserInfoEntity userInfo);

    @Select("select * from user_info where ui_update_time > #{time}")
    List<UserInfo> getUpdatedUserInfos(LocalDateTime time);

    @Select("select * from user_info where ui_token = #{token} LIMIT 1")
    UserInfo selectUserInfoByToken(String token);
}
