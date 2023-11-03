package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.entity.database.Resources;
import com.lamp.lantern.service.core.entity.database.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ResourcesMapper {

    @Select({"<script>",
            "select * from resources",
            "<where>",
            "<if test = '#{resourceId} != null'>resource_id=#{resourceId},</if>",
            "<if test = '#{resourceSystemId} != null'>resource_system_id=#{resourceSystemId},</if>",
            "<if test = '#{resourceProjectId} != null'>resource_project_id=#{resourceProjectId},</if>",
            "<if test = '#{resourceProjectName} != null'>resource_project_name=#{resourceProjectName},</if>",
            "<if test = '#{resourceModuleId} != null'>resource_module_id=#{resourceModuleId},</if>",
            "<if test = '#{resourceModuleName} != null'>resource_module_name=#{resourceModuleName},</if>",
            "<if test = '#{resourceType} != null'>resource_type=#{resourceType},</if>",
            "<if test = '#{resourceName} != null'>resource_name=#{resourceName},</if>",
            "<if test = '#{resourceOperator} != null'>resource_operator=#{resourceOperator},</if>",
            "<if test = '#{resourceConditions} != null'>resource_conditions=#{resourceConditions},</if>",
            "<if test = '#{resourceParentResourceId} != null'>resource_parent_resource_id=#{resourceParentResourceId},</if>",
            "</where>",
            "</script>"

    })
    public List<Resources> selectByForm(Resources resources);

    @Update({"<script>",
            "update resources",
            "<set>",
            "<if test = '#{resourceSystemId} != null'>resource_system_id=#{resourceSystemId},</if>",
            "<if test = '#{resourceProjectId} != null'>resource_project_id=#{resourceProjectId},</if>",
            "<if test = '#{resourceProjectName} != null'>resource_project_name=#{resourceProjectName},</if>",
            "<if test = '#{resourceModuleId} != null'>resource_module_id=#{resourceModuleId},</if>",
            "<if test = '#{resourceModuleName} != null'>resource_module_name=#{resourceModuleName},</if>",
            "<if test = '#{resourceType} != null'>resource_type=#{resourceType},</if>",
            "<if test = '#{resourceName} != null'>resource_name=#{resourceName},</if>",
            "<if test = '#{resourceDescription} != null'>resource_description=#{resourceDescription},</if>",
            "<if test = '#{resourceValidTime} != null'>resource_valid_time=#{resourceValidTime},</if>",
            "<if test = '#{resourceOperator} != null'>resource_operator=#{resourceOperator},</if>",
            "<if test = '#{resourceConditions} != null'>resource_conditions=#{resourceConditions},</if>",
            "<if test = '#{resourceParentResourceId} != null'>resource_parent_resource_id=#{resourceParentResourceId},</if>",
            "</set>",
            "where resource_id = #{resourceId}",
            "</script>"
    })
    public Integer updateByForm(Resources resources);

    @Insert("insert into resources(resource_system_id, resource_project_id, resource_project_name, resource_module_id, resource_module_name, resource_type, resource_name, resource_description, resource_valid_time, resource_operator, resource_conditions, resource_parent_resource_id) values(#{resourceSystemId}, #{resourceProjectId}, #{resourceProjectName}, #{resourceModuleId}, #{resourceModuleName}, #{resourceType}, #{resourceName}, #{resourceDescription}, #{resourceValidTime}, #{resourceOperator}, #{resourceConditions}, #{resourceParentResourceId})")
    public Integer insert(Resources resources);

    public List<Resources> selectListBySystemId();

    @Select("select * from resources where resource_id = #{id} and resource_is_delete = 0")
    Resources selectByResourceId(Long id);

    @Update("update resources set resource_is_delete = 1 where resource_id = #{resourceId}")
    Integer delete(Resources resources);

    @Select({"select * from ",
            "(resources join resource_role_relation on rrr_resource_id = resource_id and rrr_valid_time > now())",
            " where rrr_role_id = #{roleId} and resource_is_delete = 0 ",
            "and",
            " (select role_is_delete from role where role_id = #{roleId}) = 0"})
    List<Resources> selectValidResourcesByRoleId(Role roleEntity);


    @Select({"<script>",
            "select * from ",
            //给resource补充role信息
            "(resources join resource_role_relation on rrr_resource_id = resource_id and rrr_valid_time > now())",
            " where resource_is_delete = 0 ",
            "and rrr_role_id in ",
            "<foreach collection='list' item='roleEntity' open='(' separator=',' close=')'>",
            "#{roleEntity.roleId}",
            "</foreach>",
            "</script>"
    })
    List<Resources> selectValidResourcesByRoleIds(List<Role> roleEntityList);

    @Select("select * from resources where resource_id = #{resourceId} and resource_is_delete = 0")
    Resources selectById(Resources resources);

    @Select("select * from resources where resource_module_id = #{resourceModuleId} and resource_is_delete = 0")
    Resources selectByProjectId(Resources resources);

    @Select("select * from resources where resource_module_id = #{resourceModuleId} and resource_is_delete = 0")
    Resources selectByModuleId(Resources resources);

    @Update({
            "<script>",
            "update resources set resource_is_delete = 1 ",
            "where resource_id in ",
            "<foreach collection='resources' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.resourceId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteResources(List<ResourcesEntity> resources);
}
