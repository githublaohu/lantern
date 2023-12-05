package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.UserRoleRelationEntity;
import com.lamp.lantern.plugins.api.mode.UserRoleRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleRelationMapper {
    static String urr_is_valid = "urr_start_time < now() and urr_end_time > now() and urr_valid_time > now() and is_delete = 0";

    @Options(useGeneratedKeys = true, keyProperty = "urrId", keyColumn = "urr_id")
    @Insert("insert into `user_role_relation` (urr_type,role_id,user_id, urr_start_time,urr_valid_time,urr_create_user_id,urr_update_user_id) values (#{urrType},#{roleId},#{userId},#{urrStartTime},#{urrValidTime},#{operatorId},#{operatorId})")
    Integer insertUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation set urr_end_time = now() where urr_id = #{urrId}")
   Integer endUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation set urr_valid_time = #{urrValidTime} where urr_id = #{urrId} and"+urr_is_valid)
    Integer changeValidTime(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation set urr_valid_time = #{urrValidTime} where user_id = #{urrUserId} and"+urr_is_valid)
    Integer updateUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update({
            "<script>",
            "update user_role_relation set urr_end_time = now() ",
            "where urr_user_id in ",
            "<foreach collection='userRoleRelationEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.urrUserId}",
            "</foreach>",
            "</script>"
    })
    Integer endUserRoleRelations(List<UserRoleRelationEntity> userRoleRelationEntities);


    @Select("select * from user_role_relation where "+urr_is_valid)
    List<UserRoleRelationEntity> selectValidUserRoleRelations();

}
