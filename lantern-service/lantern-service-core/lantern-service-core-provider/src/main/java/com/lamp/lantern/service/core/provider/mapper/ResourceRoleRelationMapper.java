package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.ResourceRoleRelationEntity;
import com.lamp.lantern.plugins.api.mode.ResourceRoleRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;



@Mapper
public interface ResourceRoleRelationMapper {

    @Options(useGeneratedKeys = true, keyProperty = "rrrId", keyColumn = "rrr_id")
    @Insert({
            "<script>",
            "insert into resource_role_relation (",
            "resource_id,role_id,",
            "<if test='rrrType != null'>rrr_type,</if>",
            "<if test='rrrTypeId != null'>rrr_type_id,</if>",
            "start_time,valid_time,create_user_id,update_user_id",
            ") values (",
            "#{resourceId},#{roleId},",
            "<if test='rrrType != null'>#{rrrType},</if>",
            "<if test='rrrTypeId != null'>#{rrrTypeId},</if>",
            "#{startTime},#{validTime},#{operatorId},#{operatorId}",
            ")",
            "</script>"
    })
    public Integer insertResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Update("update resource_role_relation set resource_id = #{resourceId}, role_id = #{roleId}, rrr_type = #{rrrType}, rrr_type_id = #{rrrTypeId}, start_time = #{startTime},valid_time = #{validTime},update_user_id=#{operatorId} where rrr_id = #{rrrId}")
    public Integer updateResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where resource_id = #{resourceId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByResourceId(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where role_id = #{roleId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByRoleId(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where rrr_type_id = #{rrrTypeId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByTypeId(ResourceRoleRelation resourceRoleRelationEntity);

    @Update("update resource_role_relation set valid_time = #{validTime},start_time = #{startTime},update_user_id=#{operatorId} where rrr_id = #{rrrId}")
    Integer changeValidTime(ResourceRoleRelation resourceRoleRelationEntity);

    @Select({"<script>",
            "select * from resource_role_relation",
            "<where>",
            "<if test='rrrId != null'>rrr_id = #{rrrId},</if>",
            "<if test='resourceId != null'>resource_id = #{resourceId},</if>",
            "<if test='roleId != null'>role_id = #{roleId},</if>",
            "<if test='rrrType != null'>rrr_type = #{rrrType},</if>",
            "<if test='rrrTypeId != null'>rrr_type_id = #{rrrTypeId},</if>",
            "<if test='startTime != null'>start_time = #{startTime},</if>",
            "<if test='validTime != null'>valid_time = #{validTime},</if>",
            "<if test='rrrCreateTime != null'>rrr_create_time = #{rrrCreateTime},</if>",
            "<if test='rrrUpdateTime != null'>rrr_update_time = #{rrrUpdateTime},</if>",
            "<if test='rrrEndTime != null'>end_time = #{rrrEndTime},</if>",
            "<if test='createUserId != null'>create_user_id = #{createUserId},</if>",
            "<if test='updateUserId != null'>update_user_id = #{updateUserId},</if>",
            "<if test='isDelete != null'>is_delete = #{isDelete},</if>",
            "</where>",
            "</script>"})
    List<ResourceRoleRelation> queryResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where"+is_valid)
    //使用Start,Valid,End判断是否有效
    List<ResourceRoleRelation> getValidResourceRoleRelation();

    @Update("update resource_role_relation set is_delete = 1, update_user_id=#{operatorId} where rrr_id = #{rrrId}")
    Integer deleteResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Update({
            "<script>",
            "update resource_role_relation set end_time = now() ",
            "where resource_id in ",
            "<foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.resourceId}",
            "</foreach>",
            "</script>"
    })
    Integer endResourceRoleRelations(List<ResourceRoleRelationEntity> resourceRoleRelationEntities);

    //TODO: 本来设想使用virtual column放到数据库里，但是idea好像不支持，先试用字符串表示，用那个`@Sql`也好。
    String is_valid = "start_time < now() and valid_time > now() and end_time > now() and is_delete = 0";
}
