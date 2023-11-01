package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.PlatformUserInfoEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create table lantern_v2.platform_user_info
 * (
 *     pui_id          bigint auto_increment
 *         primary key,
 *     pui_user_id     bigint                                               not null,
 *     pui_open_id     bigint                                               not null,
 *     pui_type        enum ('PLATFORM', 'SECOND', 'THIRD') default 'THIRD' not null,
 *     pui_authchannel enum ('Github', 'Alipay', 'Wechat', 'Taobao', 'QQ')  not null,
 *     pui_union_id    bigint                               default 0       not null
 * );
 */
@Mapper
public interface PlatformUserInfoMapper {
    @Insert("insert into platform_user_info values (#{puiId}, #{puiUserId}, #{puiOpenId}, #{puiType}, #{puiAuthchannel}, #{puiUnionId})")
    public Integer insertPlatformUserInfo(PlatformUserInfo platformUserInfo);


    @Select("select * from platform_user_info where pui_user_id and pui_open_id LIMIT 1;")
    UserInfo checkPlatformUserByUserIdAndTriId( PlatformUserInfoEntity platformUserInfoEntity);

    @Select("select * from user_info where ui_id = (select pui_user_id from platform_user_info where pui_open_id = #{puiOpenId} and pui_authchannel = #{puiAuthchannel} LIMIT 1)")
    UserInfoEntity checkUserByTriIdAndAuthchannel(PlatformUserInfoEntity platformUserInfoEntity);
}
