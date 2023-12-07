package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.mode.LoginRecord;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface LoginRecordMapper {

    @Options(useGeneratedKeys = true, keyProperty = "ulId", keyColumn = "ul_id")
    @Insert({
            "<script>",
            "insert into user_login_record (",
            "system_id,",
            "<if test='triId != null'>tri_id,</if>",
            "<if test='productId != null'>product_id,</if>",
            "<if test='ulLoginTime != null'>ul_login_time,</if>",
            "<if test='ulLoginAddress != null'>ul_login_address,</if>",
            "<if test='ulLoginIp != null'>ul_login_ip,</if>",
            "<if test='ulLoginDeviceType != null'>ul_login_device_type,</if>",
            "<if test='ulLoginDevice != null'>ul_login_device,</if>",
            "<if test='ulLoginDeviceModel != null'>ul_login_device_model,</if>",
            "<if test='ulLoginSystem != null'>ul_login_system,</if>",
            "<if test='ulLoginSystemModel != null'>ul_login_system_model,</if>",
            "<if test='ulSessionId != null'>ul_session_id,</if>",
            "<if test='ulLoginWay != null'>ul_login_way,</if>",
            "<if test='ulLoginWayPlatform != null'>ul_login_way_platform,</if>",
            "<if test='ulLoginTerminal != null'>ul_login_terminal,</if>",
            "<if test='ulLoginTerminalModel != null'>ul_login_terminal_model,</if>",
            "<if test='ulQuitWay != null'>ul_quit_way,</if>",
            "<if test='ulQuitIp != null'>ul_quit_ip,</if>",
            "<if test='ulQuitTime != null'>ul_quit_time,</if>",
            "<if test='ulQuitAddress != null'>ul_quit_address,</if>",
            "<if test='ulLoginStatus != null'>ul_login_status,</if>",
            "<if test='ulLoginFailReason != null'>ul_login_fail_reason,</if>",
            "ui_id",
            ") values (",
            "#{systemId},",
            "<if test='triId != null'>#{triId},</if>",
            "<if test='productId != null'>#{productId},</if>",
            "<if test='ulLoginTime != null'>#{ulLoginTime},</if>",
            "<if test='ulLoginAddress != null'>#{ulLoginAddress},</if>",
            "<if test='ulLoginIp != null'>#{ulLoginIp},</if>",
            "<if test='ulLoginDeviceType != null'>#{ulLoginDeviceType},</if>",
            "<if test='ulLoginDevice != null'>#{ulLoginDevice},</if>",
            "<if test='ulLoginDeviceModel != null'>#{ulLoginDeviceModel},</if>",
            "<if test='ulLoginSystem != null'>#{ulLoginSystem},</if>",
            "<if test='ulLoginSystemModel != null'>#{ulLoginSystemModel},</if>",
            "<if test='ulSessionId != null'>#{ulSessionId},</if>",
            "<if test='ulLoginWay != null'>#{ulLoginWay},</if>",
            "<if test='ulLoginWayPlatform != null'>#{ulLoginWayPlatform},</if>",
            "<if test='ulLoginTerminal != null'>#{ulLoginTerminal},</if>",
            "<if test='ulLoginTerminalModel != null'>#{ulLoginTerminalModel},</if>",
            "<if test='ulQuitWay != null'>#{ulQuitWay},</if>",
            "<if test='ulQuitIp != null'>#{ulQuitIp},</if>",
            "<if test='ulQuitTime != null'>#{ulQuitTime},</if>",
            "<if test='ulQuitAddress != null'>#{ulQuitAddress},</if>",
            "<if test='ulLoginStatus != null'>#{ulLoginStatus},</if>",
            "<if test='ulLoginFailReason != null'>#{ulLoginFailReason},</if>",
            "#{uiId}",
            ")",
            "</script>"
    })
    Integer insertLoginRecord(LoginRecordEntity loginRecordEntity);

    @Results(
            id = "loginRecordMap",
            value = {
                    @Result(column = "ul_id", property = "ulId"), @Result(column = "system_id", property = "systemId"), @Result(column = "ui_id", property = "uiId"), @Result(column = "tri_id", property = "triId"), @Result(column = "product_id", property = "productId"), @Result(column = "ul_login_time", property = "ulLoginTime"), @Result(column = "ul_login_address", property = "ulLoginAddress"), @Result(column = "ul_login_ip", property = "ulLoginIp"), @Result(column = "ul_login_device_type", property = "ulLoginDeviceType"), @Result(column = "ul_login_device", property = "ulLoginDevice"), @Result(column = "ul_login_device_model", property = "ulLoginDeviceModel"), @Result(column = "ul_login_system", property = "ulLoginSystem"), @Result(column = "ul_login_system_model", property = "ulLoginSystemModel"), @Result(column = "ul_session_id", property = "ulSessionId"), @Result(column = "ul_login_way", property = "ulLoginWay"), @Result(column = "ul_login_way_platform", property = "ulLoginWayPlatform"), @Result(column = "ul_login_terminal", property = "ulLoginTerminal"), @Result(column = "ul_login_terminal_model", property = "ulLoginTerminalModel"), @Result(column = "ul_quit_way", property = "ulQuitWay"), @Result(column = "ul_quit_ip", property = "ulQuitIp"), @Result(column = "ul_quit_time", property = "ulQuitTime"), @Result(column = "ul_quit_address", property = "ulQuitAddress"), @Result(column = "ul_login_status", property = "ulLoginStatus"), @Result(column = "ul_login_fail_reason", property = "ulLoginFailReason")}
    )
    @Select({"select * from user_login_record where ui_id = #{uiId}"})
    public List<LoginRecordEntity> checkLoginRecordEntityByUserId(UserInfoEntity userInfoEntity);

    @Update({"update user_login_record set ul_quit_time = #{ulQuitTime} where ul_id = #{ulId}"})
    public Integer updateLoginRecordEntityExitTimeField(LoginRecordEntity loginRecordEntity);

    @Update({"update user_login_record set ul_quit_way = #{ulQuitWay} where ul_id = #{ulId}"})
    public Integer updateLoginRecordEntityQuitWayField(LoginRecordEntity loginRecordEntity);

    @Select("select * from user_login_record")
    List<LoginRecordEntity> getAllLoginRecordEntity();
}
