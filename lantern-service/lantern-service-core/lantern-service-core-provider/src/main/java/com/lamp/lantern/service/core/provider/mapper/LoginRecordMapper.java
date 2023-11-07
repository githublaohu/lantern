package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.mode.LoginRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface LoginRecordMapper {

    @Options(useGeneratedKeys = true, keyProperty = "loginRecordEntity.ulId", keyColumn = "ul_id")
    @Insert({"insert into userlogin_record (ui_id, ul_login_time, ul_login_address, ul_login_ip, ul_login_system, ul_login_way, ul_login_terminal,  tri_id, ul_login_status,ul_session_id) values (#{loginRecordEntity.uiId}, #{loginRecordEntity.ulLoginTime}, #{loginRecordEntity.ulLoginAddress}, #{loginRecordEntity.ulLoginIp},#{loginRecordEntity.ulLoginSystem}, #{loginRecordEntity.ulLoginWay}, #{loginRecordEntity.ulLoginTerminal},  #{loginRecordEntity.triId}, #{loginRecordEntity.ulLoginStatus},#{loginRecordEntity.ulSessionId})"})
    public Integer insertLoginRecord(@Param("loginRecordEntity") LoginRecord loginRecordEntity);

    @Results(
            id = "loginRecordMap",
            value = {
                    @Result(column = "ul_id", property = "ulId"), @Result(column = "ui_id", property = "uiId"), @Result(column = "ul_login_time", property = "ulLoginTime"),
                    @Result(column = "ul_exit_time", property = "ulExitTime"), @Result(column = "ul_login_address", property = "ulLoginAddress"), @Result(column = "ul_login_ip", property = "ulLoginIp"),
                    @Result(column = "ul_login_device", property = "ulLoginDevice"), @Result(column = "ul_login_device_model", property = "ulLoginDeviceModel"), @Result(column = "ul_login_system", property = "ulLoginSystem"), @Result(column = "ul_login_way", property = "ulLoginWay"),
                    @Result(column = "ul_login_terminal", property = "ulLoginTerminal"), @Result(column = "ul_quit_way", property = "ulQuitWay"), @Result(column = "tri_id", property = "triId"), @Result(column = "ul_login_status", property = "ulLoginStatus")
            }
    )
    @Select({"select * from userlogin_record where ui_id = #{userInfoEntity.uiId}"})
    public List<LoginRecord> checkLoginRecordEntityByUserId(UserInfo userInfoEntity);

    @Update({"update userlogin_record set ul_exit_time = #{loginRecordEntity.ulExitTime} where ul_id = #{loginRecordEntity.ulId}"})
    public Integer updateLoginRecordEntityExitTimeField(LoginRecord loginRecordEntity);

    @Update({"update userlogin_record set ul_quit_way = #{loginRecordEntity.ulQuitWay} where ul_id = #{loginRecordEntity.ulId}"})
    public Integer updateLoginRecordEntityQuitWayField(LoginRecord loginRecordEntity);

    @Select("select * from userlogin_record")
    List<LoginRecord> getAllLoginRecordEntity();
}
