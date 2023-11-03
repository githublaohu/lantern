package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.ResourceRoleRelationEntity;
import com.lamp.lantern.service.core.entity.database.ResourceRoleRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface ResourceRoleRelationMapper {

    @Insert("insert into resource_role_relation(rrr_resource_id, rrr_role_id, rrr_operator_id,rrr_type, rrr_type_id) values(#{rrrResourceId}, #{rrrRoleId}, #{rrrOperatorId}, #{rrrType}, #{rrrTypeId})")
    public Integer insertResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Update("update resource_role_relation set rrr_resource_id = #{rrrResourceId}, rrr_operator_id = #{rrrOperatorId}, rrr_role_id = #{rrrRoleId}, rrr_type = #{rrrType}, rrr_type_id = #{rrrTypeId} where rrr_id = #{rrrId}")
    public Integer updateResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where rrr_resource_id = #{rrrResourceId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByResourceId(ResourceRoleRelation resourceRoleRelationEntity);
    @Select("select * from resource_role_relation where rrr_role_id = #{rrrRoleId}")
    public List<ResourceRoleRelation> getResourceRoleRelationByRoleId(ResourceRoleRelation resourceRoleRelationEntity);

    @Update("update resource_role_relation set rrr_valid_time = #{rrrValidTime} where rrr_id = #{rrrId}")
    Integer changeValidTime(ResourceRoleRelation resourceRoleRelationEntity);

    @Select({"<script>",
            "select * from resource_role_relation",
            "<where>",
            "<if test='#{rrrId} != null'>rrr_id = #{rrrId},</if>",
            "<if test='#{rrrResourceId} != null'>rrr_resource_id = #{rrrResourceId},</if>",
            "<if test='#{rrrOperatorId} != null'>rrr_operator_id = #{rrrOperatorId},</if>",
            "<if test='#{rrrRoleId} != null'>rrr_role_id = #{rrrRoleId},</if>",
            "<if test='#{rrrType} != null'>rrr_type = #{rrrType},</if>",
            "<if test='#{rrrTypeId} != null'>rrr_type_id = #{rrrTypeId},</if>",
            "<if test='#{rrrCreateTime} != null'>rrr_create_time = #{rrrCreateTime},</if>",
            "<if test='#{rrrUpdateTime} != null'>rrr_update_time = #{rrrUpdateTime},</if>",
            "<if test='#{rrrEndTime} != null'>rrr_end_time = #{rrrEndTime},</if>",
            "<if test='#{rrrValidTime} != null'>rrr_valid_time = #{rrrValidTime},</if>",
            "</where>",
            "</script>"})
    List<ResourceRoleRelation> queryResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Select("select * from resource_role_relation where rrr_valid_time > now()")
    //只使用valid_time判断合法性
    List<ResourceRoleRelation> getValidResourceRoleRelation();

    @Update("update resource_role_relation set rrr_end_time = #{rrrEndTime}, rrr_valid_time = #{rrrValidTime} where rrr_id = #{rrrId}")
    Integer deleteResourceRoleRelation(ResourceRoleRelation resourceRoleRelationEntity);

    @Update({
            "<script>",
            "update resource_role_relation set rrr_valid_time = now() ",
            "where rrr_resource_id in ",
            "<foreach collection='resourceRoleRelationEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.rrrResourceId}",
            "</foreach>",
            "</script>"
    })
    Integer endResourceRoleRelations(List<ResourceRoleRelationEntity> resourceRoleRelationEntities);
}
