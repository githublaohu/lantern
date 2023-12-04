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
    @Options(useGeneratedKeys = true, keyProperty = "platformUserInfo.puiId", keyColumn = "pui_id")
    @Insert("insert into platform_user_info (system_id, user_id, corporation_id, app_id, pui_open_id, pui_union_id, pui_type, pui_auth_channel,pui_create_user_id,pui_update_user_id) values (#{platformUserInfo.systemId}, #{platformUserInfo.userId}, #{platformUserInfo.corporationId}, #{platformUserInfo.appId}, #{platformUserInfo.puiOpenId}, #{platformUserInfo.puiUnionId}, #{platformUserInfo.puiType}, #{platformUserInfo.puiAuthchannel}, #{platformUserInfo.operatorId},#{platformUserInfo.operatorId})")
    public Integer insertPlatformUserInfo(PlatformUserInfo platformUserInfo);


    @Select("select * from platform_user_info where user_id=#{platformUserInfoEntity.userId} and pui_open_id=#{platformUserInfoEntity.puiOpenId} and pui_type='THIRD' and " +is_valid+" LIMIT 1")
    UserInfo checkPlatformUserByUserIdAndTriId(PlatformUserInfoEntity platformUserInfoEntity);

    @Select("select * from platform_user_info where pui_open_id=#{platformUserInfoEntity.puiOpenId} and pui_auth_channel=#{platformUserInfoEntity.puiAuthchannel} and pui_type='THIRD' and "+is_valid+" LIMIT 1")
    UserInfoEntity checkUserByTriIdAndAuthchannel(PlatformUserInfoEntity platformUserInfoEntity);

    String is_valid = "pui_valid_time > now() and pui_start_time < now() and is_delete = 0";
}


