package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.UserRoleRelationEntity;
import com.lamp.lantern.plugins.api.mode.UserRoleRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleRelationMapper {
    static String urr_is_valid = "urr.start_time < now() and (urr.end_time = urr.create_time or urr.end_time > now()) and urr.valid_time > now() and urr.is_delete = 0";

    @Options(useGeneratedKeys = true, keyProperty = "urrId", keyColumn = "urr_id")
    @Insert({
            "<script>",
            "insert into user_role_relation (",
            "urr_type,role_id,user_id,",
            "<if test='startTime != null'>start_time,</if>",
            "<if test='validTime != null'>valid_time,</if>",
            "create_user_id,update_user_id",
            ") values (",
            "#{urrType},#{roleId},#{userId},",
            "<if test='startTime != null'>#{startTime},</if>",
            "<if test='validTime != null'>#{validTime},</if>",
            "#{operatorId},#{operatorId}",
            ")",
            "</script>"
    })
    Integer insertUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation set end_time = now() where urr_id = #{urrId}")
   Integer endUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation as urr set valid_time = #{validTime} where urr_id = #{urrId} and"+urr_is_valid)
    Integer changeValidTime(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation as urr set valid_time = #{validTime} where user_id = #{userId} and"+urr_is_valid)
    Integer updateUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update({
            "<script>",
            "update user_role_relation set end_time = now() ",
            "where user_id in ",
            "<foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.userId}",
            "</foreach>",
            "</script>"
    })
    Integer endUserRoleRelations(List<UserRoleRelationEntity> userRoleRelationEntities);


    @Select("select * from user_role_relation as urr where "+urr_is_valid)
    List<UserRoleRelationEntity> selectValidUserRoleRelations();

    @Update("update user_role_relation set is_delete = 1 where urr_id = #{urrId}")
    Integer deleteUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity);
}
