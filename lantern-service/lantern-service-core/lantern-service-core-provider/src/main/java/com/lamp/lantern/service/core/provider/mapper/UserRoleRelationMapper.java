package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.UserRoleRelationEntity;
import com.lamp.lantern.plugins.api.mode.UserRoleRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserRoleRelationMapper {
    @Insert("insert into `user_role_relation` (urr_role_id, urr_user_id,urr_valid_time) values (#{urrRoleId}, #{urrUserId},#{urrValidTime})")
    Integer insert(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation set urr_end_time = now(),urr_valid_time = now() where urr_id = #{urrId}")
   Integer endUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation set urr_valid_time = #{urrValidTime} where urr_id = #{urrId}")
    Integer changeValidTime(UserRoleRelation userRoleRelationEntity);

    @Update("update user_role_relation set urr_valid_time = #{urrValidTime} where urr_user_id = #{urrUserId}")
    Integer updateUserRoleRelation(UserRoleRelation userRoleRelationEntity);

    @Update({
            "<script>",
            "update user_role_relation set urr_valid_time = now(),urr_end_time = now() ",
            "where urr_user_id in ",
            "<foreach collection='userRoleRelationEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.urrUserId}",
            "</foreach>",
            "</script>"
    })
    Integer endUserRoleRelations(List<UserRoleRelationEntity> userRoleRelationEntities);

}
