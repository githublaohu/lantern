package com.lamp.lantern.service.core.provider.mapper;


import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * CREATE TABLE `user_info` (
 *   `ui_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户Id',
 *   `ui_name` varchar(16) NOT NULL COMMENT '用户名',
 *   `ui_nickname` varchar(16) NOT NULL DEFAULT `ui_name` COMMENT '用户昵称',
 *   `ui_idcard` varchar(20) NOT NULL COMMENT '用户唯一标识符',
 *   `ui_phone` varchar(12) NOT NULL DEFAULT '' COMMENT '用户联系方式',
 *   `ui_email` varchar(32) NOT NULL DEFAULT '' COMMENT '用户邮箱',
 *   `ui_head_portrait` varchar(32) DEFAULT '' COMMENT '用户图像',
 *   `ui_lack_flag` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '用户缺失字段标志位',
 *   `ui_sex` enum('MALE','FEMALE','UNKNOW') DEFAULT NULL COMMENT '用户性别',
 *   `ui_birth` date NOT NULL DEFAULT '1970-01-01' COMMENT '用户生日',
 *   `ui_address` varchar(32) NOT NULL DEFAULT '' COMMENT '用户家庭地址',
 *   `ui_salt` varchar(64) NOT NULL DEFAULT '' COMMENT '用户盐',
 *   `ui_salt_password` varchar(64) NOT NULL DEFAULT '' COMMENT '用户盐密',
 *   `ui_login_address` varchar(32) NOT NULL DEFAULT '' COMMENT '用户最近登录地址',
 *   `ui_login_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '用户最近登录时间',
 *   `ui_exit_time` datetime DEFAULT NULL COMMENT '用户最近退出时间',
 *   `ui_first_login` enum('True','False') DEFAULT 'True' COMMENT '用户是否为第一次登录',
 *   `tri_id` bigint(20) unsigned DEFAULT 0 COMMENT '用户第三方信息Id',
 *   `ui_status` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE' COMMENT '用户状态',
 *   `allow_login` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE' COMMENT '是否限制登录',
 *   PRIMARY KEY (`ui_id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
 */
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
        "<if test='#{triId} != null'>tri_id,</if>",
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
        "<if test='#{triId} != null'>#{triId},</if>",
        "<if test='#{uiStatus} != null'>#{uiStatus},</if>",
        "<if test='#{allowLogin} != null'>#{allowLogin},</if>",
        "#{uiSalt}, #{uiSaltPassword}",
        ")",
        "</script>"
        })
    //TODO:有问题, 不能检测到null
public UserInfoEntity registerUserInfoEntity(UserInfo userInfoEntity);

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
}
