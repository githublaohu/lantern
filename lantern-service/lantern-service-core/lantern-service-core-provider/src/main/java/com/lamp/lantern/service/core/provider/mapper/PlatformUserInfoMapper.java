package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.PlatformUserInfoEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface PlatformUserInfoMapper {
    /** Resolve only explicitly bound members, isolated by system, corporation and application. */
    @Select("SELECT u.ui_id AS uiId,u.system_id AS systemId,u.ui_name AS uiName,u.ui_nickname AS uiNickname"
            + " FROM platform_user_info p JOIN user_info u ON u.ui_id=p.user_id AND u.system_id=p.system_id"
            + " WHERE p.system_id=#{systemId} AND BINARY p.corporation_id=BINARY #{corpId}"
            + " AND BINARY p.app_id=BINARY #{agentId} AND BINARY p.pui_open_id=BINARY #{memberId}"
            + " AND p.pui_type='PLATFORM' AND BINARY p.pui_auth_channel=BINARY 'EnterpriseWechat'"
            + " AND p.pui_status=0 AND p.is_delete=0 AND u.is_delete=0"
            + " AND u.ui_allow_login='ACTIVE' LIMIT 2")
    @Options(useCache = false, flushCache = Options.FlushCachePolicy.TRUE)
    List<UserInfo> findEnterpriseWechatUsers(@Param("systemId") Long systemId, @Param("corpId") String corpId,
            @Param("agentId") String agentId, @Param("memberId") String memberId);

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


