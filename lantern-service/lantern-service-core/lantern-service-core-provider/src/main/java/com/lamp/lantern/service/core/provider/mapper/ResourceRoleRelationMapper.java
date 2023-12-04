package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.ResourceRoleRelationEntity;
import com.lamp.lantern.plugins.api.mode.ResourceRoleRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;



@Mapper
public interface ResourceRoleRelationMapper {

    @Options(useGeneratedKeys = true, keyProperty = "rrrId", keyColumn = "rrr_id")
    @Insert("insert into resource_role_relation(rrr_resource_id, rrr_role_id, rrr_type, rrr_type_id, rrr_start_time,rrr_valid_time,rrr_create_user_id,rrr_update_user_id) values (#{rrrResourceId},  #{rrrRoleId}, #{rrrType}, #{rrrTypeId},#{rrrStartTime},#{rrrValidTime},#{operatorId},#{operatorId})")
    public Integer insertResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Update("update resource_role_relation set rrr_resource_id = #{rrrResourceId}, rrr_role_id = #{rrrRoleId}, rrr_type = #{rrrType}, rrr_type_id = #{rrrTypeId}, rrr_start_time = #{rrrStartTime},rrr_valid_time = #{rrrValidTime},rrr_update_user_id=#{operatorId} where rrr_id = #{rrrId}")
    public Integer updateResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where rrr_resource_id = #{rrrResourceId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByResourceId(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where rrr_role_id = #{rrrRoleId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByRoleId(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where rrr_type_id = #{rrrTypeId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByTypeId(ResourceRoleRelation resourceRoleRelationEntity);

    @Update("update resource_role_relation set rrr_valid_time = #{rrrValidTime},rrr_start_time = #{rrrStartTime},rrr_update_user_id=#{operatorId} where rrr_id = #{rrrId}")
    Integer changeValidTime(ResourceRoleRelation resourceRoleRelationEntity);

    @Select({"<script>",
            "select * from resource_role_relation",
            "<where>",
            "<if test='#{rrrId} != null'>rrr_id = #{rrrId},</if>",
            "<if test='#{rrrResourceId} != null'>rrr_resource_id = #{rrrResourceId},</if>",
            "<if test='#{rrrRoleId} != null'>rrr_role_id = #{rrrRoleId},</if>",
            "<if test='#{rrrType} != null'>rrr_type = #{rrrType},</if>",
            "<if test='#{rrrTypeId} != null'>rrr_type_id = #{rrrTypeId},</if>",
            "<if test='#{rrrStartTime} != null'>rrr_start_time = #{rrrStartTime},</if>",
            "<if test='#{rrrValidTime} != null'>rrr_valid_time = #{rrrValidTime},</if>",
            "<if test='#{rrrCreateTime} != null'>rrr_create_time = #{rrrCreateTime},</if>",
            "<if test='#{rrrUpdateTime} != null'>rrr_update_time = #{rrrUpdateTime},</if>",
            "<if test='#{rrrEndTime} != null'>rrr_end_time = #{rrrEndTime},</if>",
            "<if test='#{rrrCreateUserId} != null'>rrr_create_user_id = #{rrrCreateUserId},</if>",
"<if test='#{rrrUpdateUserId} != null'>rrr_update_user_id = #{rrrUpdateUserId},</if>",
            "<if test='#{isDelete} != null'>is_delete = #{isDelete},</if>",
            "</where>",
            "</script>"})
    List<ResourceRoleRelation> queryResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where"+is_valid)
    //使用Start,Valid,End判断是否有效
    List<ResourceRoleRelation> getValidResourceRoleRelation();

    @Update("update resource_role_relation set is_delete = 1, rrr_update_user_id=#{operatorId} where rrr_id = #{rrrId}")
    Integer deleteResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Update({
            "<script>",
            "update resource_role_relation set rrr_end_time = now() ",
            "where rrr_resource_id in ",
            "<foreach collection='resourceRoleRelationEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.rrrResourceId}",
            "</foreach>",
            "</script>"
    })
    Integer endResourceRoleRelations(List<ResourceRoleRelationEntity> resourceRoleRelationEntities);

    //TODO: 本来设想使用virtual column放到数据库里，但是idea好像不支持，先试用字符串表示，用那个`@Sql`也好。
    String is_valid = "rrr_start_time < now() and rrr_valid_time > now() and rrr_end_time > now() and is_delete = 0";
}
