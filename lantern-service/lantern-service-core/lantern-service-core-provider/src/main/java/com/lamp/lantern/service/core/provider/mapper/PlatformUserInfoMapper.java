package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.PlatformUserInfoEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface PlatformUserInfoMapper {
    @Options(useGeneratedKeys = true, keyProperty = "puiId", keyColumn = "pui_id")
    @Insert({
            "<script>",
            "insert into platform_user_info (",
            "system_id, ",
            "<if test='userId != null'>user_id,</if>",
            "<if test='corporationId != null'>corporation_id,</if>",
            "<if test='appId != null'>app_id,</if>",
            "<if test='puiOpenId != null'>pui_open_id,</if>",
            "<if test='puiUnionId != null'>pui_union_id,</if>",
            "<if test='puiType != null'>pui_type,</if>",
            "<if test='puiAuthChannel != null'>pui_auth_channel,</if>",
            "create_user_id,update_user_id",
            ") values (",
            "#{systemId},",
            "<if test='userId != null'>#{userId},</if>",
            "<if test='corporationId != null'>#{corporationId},</if>",
            "<if test='appId != null'>#{appId},</if>",
            "<if test='puiOpenId != null'>#{puiOpenId},</if>",
            "<if test='puiUnionId != null'>#{puiUnionId},</if>",
            "<if test='puiType != null'>#{puiType},</if>",
            "<if test='puiAuthChannel != null'>#{puiAuthChannel},</if>",
            "#{operatorId},#{operatorId}",
            ")",
            "</script>"
    })
    Integer insertPlatformUserInfo(PlatformUserInfo platformUserInfo);


    @Select("select * from platform_user_info where user_id=#{platformUserInfoEntity.userId} and pui_open_id=#{platformUserInfoEntity.puiOpenId} and pui_type='THIRD' and " +is_valid+" LIMIT 1")
    UserInfo checkPlatformUserByUserIdAndTriId(PlatformUserInfoEntity platformUserInfoEntity);

    @Select("select * from platform_user_info where pui_open_id=#{platformUserInfoEntity.puiOpenId} and pui_auth_channel=#{platformUserInfoEntity.puiAuthChannel} and pui_type='THIRD' and "+is_valid+" LIMIT 1")
    UserInfoEntity checkUserByTriIdAndAuthchannel(PlatformUserInfoEntity platformUserInfoEntity);

    String is_valid = "pui_valid_time > now() and pui_start_time < now() and is_delete = 0";
}


