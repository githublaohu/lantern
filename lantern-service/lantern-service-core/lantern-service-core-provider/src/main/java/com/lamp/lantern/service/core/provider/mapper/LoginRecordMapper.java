package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface LoginRecordMapper {

    static String TABLE_SQL = "login_record";

    static String INSERT_SQL = "insert into " + TABLE_SQL;

    static String SELECT_SQL = "select * from " + TABLE_SQL + " where ";

    static String UPDATE_SQL = "update " + TABLE_SQL + "set ";


    @Options(useGeneratedKeys = true, keyProperty = "loginRecordEntity.ulId", keyColumn = "ul_id")
    @Insert({INSERT_SQL,
            "(ui_id,ul_login_address,ul_login_ip,ul_login_device,ul_login_device_model,ul_login_system,ul_login_way,ul_login_terminal,ul_quit_way,tri_id,ul_login_status)values",
            "(#{loginRecordEntity.uiId},#{loginRecordEntity.ulLoginAddress},#{loginRecordEntity.ulLoginIp},#{loginRecordEntity.ulLoginDevice},#{loginRecordEntity.ulLoginDeviceModel},#{loginRecordEntity.ulLoginSystem}, #{loginRecordEntity.ulLoginWay},#{loginRecordEntity.ulLoginTerminal},#{loginRecordEntity.ulQuitWay},#{loginRecordEntity.triId},#{loginRecordEntity.ulLoginStatus})"
    })
    public Integer insertLoginRecord(@Param("loginRecordEntity") LoginRecordEntity loginRecordEntity);

    @Results(
            id = "loginRecordMap",
            value = {
                    @Result(column = "ul_id", property = "ulId"), @Result(column = "ui_id", property = "uiId"), @Result(column = "ul_login_time", property = "ulLoginTime"),
                    @Result(column = "ul_exit_time", property = "ulExitTime"), @Result(column = "ul_login_address", property = "ulLoginAddress"), @Result(column = "ul_login_ip", property = "ulLoginIp"),
                    @Result(column = "ul_login_device", property = "ulLoginDevice"), @Result(column = "ul_login_device_model", property = "ulLoginDeviceModel"), @Result(column = "ul_login_system", property = "ulLoginSystem"), @Result(column = "ul_login_way", property = "ulLoginWay"),
                    @Result(column = "ul_login_terminal", property = "ulLoginTerminal"), @Result(column = "ul_quit_way", property = "ulQuitWay"), @Result(column = "tri_id", property = "triId"), @Result(column = "ul_login_status", property = "ulLoginStatus")
            }
    )
    @Select({SELECT_SQL,
            "ui_id = #{uiId}"

    })
    public List<LoginRecordEntity> checkLoginRecordEntitysByUserId(UserInfoEntity userInfoEntity);

    @Update({UPDATE_SQL,
            "ul_exit_time = #{ulExitTime} where ul_id = #{ulId}"

    })
    public Integer updateLoginRecordEntityExitTimeField(LoginRecordEntity loginRecordEntity);

    @Update({UPDATE_SQL,
            "ul_quit_way = #{ulQuitWay} where ul_id = #{ulId}"

    })
    public Integer updateLoginRecordEntityQuitWayField(LoginRecordEntity loginRecordEntity);

}
