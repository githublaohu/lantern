package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.mode.LoginRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface LoginRecordMapper {

    @Options(useGeneratedKeys = true, keyProperty = "loginRecordEntity.ulId", keyColumn = "ul_id")
    @Insert({"insert into user_login_record (system_id, ui_id, tri_id, product_id, ul_login_time, ul_login_address, ul_login_ip, ul_login_device_type, ul_login_device, ul_login_device_model, ul_login_system, ul_login_system_model, ul_session_id, ul_login_way, ul_login_way_platform, ul_login_terminal, ul_login_terminal_model, ul_quit_way, ul_quit_ip, ul_quit_time, ul_quit_address, ul_login_status, ul_login_fail_reason ) values (#{loginRecordEntity.systemId}, #{loginRecordEntity.uiId}, #{loginRecordEntity.triId}, #{loginRecordEntity.productId}, #{loginRecordEntity.ulLoginTime}, #{loginRecordEntity.ulLoginAddress}, #{loginRecordEntity.ulLoginIp}, #{loginRecordEntity.ulLoginDeviceType}, #{loginRecordEntity.ulLoginDevice}, #{loginRecordEntity.ulLoginDeviceModel}, #{loginRecordEntity.ulLoginSystem}, #{loginRecordEntity.ulLoginSystemModel}, #{loginRecordEntity.ulSessionId}, #{loginRecordEntity.ulLoginWay}, #{loginRecordEntity.ulLoginWayPlatform}, #{loginRecordEntity.ulLoginTerminal}, #{loginRecordEntity.ulLoginTerminalModel}, #{loginRecordEntity.ulQuitWay}, #{loginRecordEntity.ulQuitIp}, #{loginRecordEntity.ulQuitTime}, #{loginRecordEntity.ulQuitAddress}, #{loginRecordEntity.ulLoginStatus}, #{loginRecordEntity.ulLoginFailReason})"})
    public Integer insertLoginRecord(LoginRecord loginRecordEntity);

    @Results(
            id = "loginRecordMap",
            value = {
                    @Result(column = "ul_id", property = "ulId"), @Result(column = "system_id", property = "systemId"), @Result(column = "ui_id", property = "uiId"), @Result(column = "tri_id", property = "triId"), @Result(column = "product_id", property = "productId"), @Result(column = "ul_login_time", property = "ulLoginTime"), @Result(column = "ul_login_address", property = "ulLoginAddress"), @Result(column = "ul_login_ip", property = "ulLoginIp"), @Result(column = "ul_login_device_type", property = "ulLoginDeviceType"), @Result(column = "ul_login_device", property = "ulLoginDevice"), @Result(column = "ul_login_device_model", property = "ulLoginDeviceModel"), @Result(column = "ul_login_system", property = "ulLoginSystem"), @Result(column = "ul_login_system_model", property = "ulLoginSystemModel"), @Result(column = "ul_session_id", property = "ulSessionId"), @Result(column = "ul_login_way", property = "ulLoginWay"), @Result(column = "ul_login_way_platform", property = "ulLoginWayPlatform"), @Result(column = "ul_login_terminal", property = "ulLoginTerminal"), @Result(column = "ul_login_terminal_model", property = "ulLoginTerminalModel"), @Result(column = "ul_quit_way", property = "ulQuitWay"), @Result(column = "ul_quit_ip", property = "ulQuitIp"), @Result(column = "ul_quit_time", property = "ulQuitTime"), @Result(column = "ul_quit_address", property = "ulQuitAddress"), @Result(column = "ul_login_status", property = "ulLoginStatus"), @Result(column = "ul_login_fail_reason", property = "ulLoginFailReason")}
    )
    @Select({"select * from user_login_record where ui_id = #{userInfoEntity.uiId}"})
    public List<LoginRecord> checkLoginRecordEntityByUserId(UserInfo userInfoEntity);

    @Update({"update user_login_record set ul_quit_time = #{loginRecordEntity.ulQuitTime} where ul_id = #{loginRecordEntity.ulId}"})
    public Integer updateLoginRecordEntityExitTimeField(LoginRecord loginRecordEntity);

    @Update({"update user_login_record set ul_quit_way = #{loginRecordEntity.ulQuitWay} where ul_id = #{loginRecordEntity.ulId}"})
    public Integer updateLoginRecordEntityQuitWayField(LoginRecord loginRecordEntity);

    @Select("select * from user_login_record")
    List<LoginRecord> getAllLoginRecordEntity();
}
