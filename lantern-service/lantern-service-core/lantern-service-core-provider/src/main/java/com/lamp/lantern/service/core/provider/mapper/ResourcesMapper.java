package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResourcesMapper {

    @Select({"<script>",
            "select * from resources",
            "<where>",
            "<if test = 'resourceId != null'>resource_id=#{resourceId},</if>",
            "<if test = 'systemId != null'>system_id=#{systemId},</if>",
            "<if test = 'projectId != null'>resource_project_id=#{projectId},</if>",
            "<if test = 'projectName != null'>project_id=#{projectName},</if>",
            "<if test = 'moduleId != null'>module_id=#{moduleId},</if>",
            "<if test = 'moduleName != null'>module_name=#{moduleName},</if>",
            "<if test = 'resourceType != null'>resource_type=#{resourceType},</if>",
            "<if test = 'resourceVersion != null'>resource_version=#{resourceVersion},</if>",
            "<if test = 'resourceName != null'>resource_name=#{resourceName},</if>",
            "<if test = 'resourceCreateTime != null'>resource_create_time=#{resourceCreateTime},</if>",
            "<if test = 'startTime != null'>start_time=#{startTime},</if>",
            "<if test = 'validTime != null'>valid_time=#{validTime},</if>",
            "<if test = 'endTime != null'>end_time=#{endTime},</if>",
            "<if test = 'updateTime != null'>resource_update_time=#{updateTime},</if>",
            "<if test = 'createUserId != null'>create_user_id=#{createUserId},</if>",
            "<if test = 'updateUserId != null'>update_user_id=#{updateUserId},</if>",
            "<if test = 'resourceDescription != null'>resource_description=#{resourceDescription},</if>",
            "<if test = 'resourceTag != null'>resource_tag=#{resourceTag},</if>",
            "<if test = 'resourceOperate != null'>resource_operate=#{resourceOperate},</if>",
            "<if test = 'resourceConditions != null'>resource_conditions=#{resourceConditions},</if>",
            "<if test = 'resourceParentResourceId != null'>resource_parent_resource_id=#{resourceParentResourceId},</if>",
            "<if test = 'isDelete != null'>is_delete=#{isDelete},</if>",
            "</where>",
            "</script>"

    })
    public List<ResourcesEntity> selectByForm(Resources resources);

    @Update({"<script>",
            "update resources",
            "<set>",
            "<if test = 'systemId != null'>system_id=#{systemId},</if>",
            "<if test = 'projectId != null'>resource_project_id=#{projectId},</if>",
            "<if test = 'projectName != null'>project_id=#{projectName},</if>",
            "<if test = 'moduleId != null'>module_id=#{moduleId},</if>",
            "<if test = 'moduleName != null'>module_name=#{moduleName},</if>",
            "<if test = 'resourceType != null'>resource_type=#{resourceType},</if>",
            "<if test = 'resourceVersion != null'>resource_version=#{resourceVersion},</if>",
            "<if test = 'resourceName != null'>resource_name=#{resourceName},</if>",
            "<if test = 'startTime != null'>start_time=#{startTime},</if>",
            "<if test = 'validTime != null'>valid_time=#{validTime},</if>",
            "<if test = 'endTime != null'>end_time=#{endTime},</if>",
            "<if test = 'resourceDescription != null'>resource_description=#{resourceDescription},</if>",
            "<if test = 'resourceTag != null'>resource_tag=#{resourceTag},</if>",
            "<if test = 'resourceOperate != null'>resource_operate=#{resourceOperate},</if>",
            "<if test = 'resourceConditions != null'>resource_conditions=#{resourceConditions},</if>",
            "<if test = 'resourceParentResourceId != null'>resource_parent_resource_id=#{resourceParentResourceId},</if>",
            "<if test = 'isDelete != null'>is_delete=#{isDelete},</if>",
            "update_user_id=#{operatorId},",
            "</set>",
            "where resource_id = #{resourceId}",
            "</script>"
    })
    public Integer updateByForm(Resources resources);

    @Options(useGeneratedKeys = true, keyProperty = "resourceId", keyColumn = "resource_id")
    @Insert({
            "<script>",
            "insert into resources(",
            "start_time,valid_time,",
            "<if test = 'systemId != null'>system_id,</if>",
            "<if test = 'productId != null'>product_id,</if>",
            "<if test = 'projectId != null'>project_id,</if>",
            "<if test = 'projectName != null'>project_name,</if>",
            "<if test = 'moduleId != null'>module_id,</if>",
            "<if test = 'moduleName != null'>module_name,</if>",
            "<if test = 'resourceType != null'>resource_type,</if>",
            "<if test = 'resourceVersion != null'>resource_version,</if>",
            "<if test = 'resourceName != null'>resource_name,</if>",
            "<if test = 'resourceDescription != null'>resource_description,</if>",
            "<if test = 'resourceTag != null'>resource_tag,</if>",
            "<if test = 'resourceOperate != null'>resource_operate,</if>",
            "<if test = 'resourceConditions != null'>resource_conditions,</if>",
            "<if test = 'resourceParentResourceId != null'>resource_parent_resource_id,</if>",
            "create_user_id,update_user_id",
            ") values (",
            "#{startTime},#{validTime},",
            "<if test = 'systemId != null'>#{systemId},</if>",
            "<if test = 'productId != null'>#{productId},</if>",
            "<if test = 'projectId != null'>#{projectId},</if>",
            "<if test = 'projectName != null'>#{projectName},</if>",
            "<if test = 'moduleId != null'>#{moduleId},</if>",
            "<if test = 'moduleName != null'>#{moduleName},</if>",
            "<if test = 'resourceType != null'>#{resourceType},</if>",
            "<if test = 'resourceVersion != null'>#{resourceVersion},</if>",
            "<if test = 'resourceName != null'>#{resourceName},</if>",
            "<if test = 'resourceDescription != null'>#{resourceDescription},</if>",
            "<if test = 'resourceTag != null'>#{resourceTag},</if>",
            "<if test = 'resourceOperate != null'>#{resourceOperate},</if>",
            "<if test = 'resourceConditions != null'>#{resourceConditions},</if>",
            "<if test = 'resourceParentResourceId != null'>#{resourceParentResourceId},</if>",
            "#{operatorId},#{operatorId}",
            ")",
            "</script>"
    })
    public Integer insert(Resources resources);

    @Update("update resources set is_delete = 1,update_user_id=#{operatorId} where resource_id = #{resourceId}")
    Integer deleteResourceById(Resources resources);

    @Select({
            "SELECT resources.* FROM resources",
            "JOIN resource_role_relation AS rrr ON resources.resource_id = rrr.rrr_resource_id",
            "JOIN role ON role.role_id = rrr.rrr_role_id",
            "WHERE rrr.is_delete = 0",
            "AND rrr.rrr_valid_time > NOW()",
            "AND rrr.rrr_start_time < NOW()",
            "AND rrr.rrr_end_time > NOW()",
            "AND resources.is_delete = 0",
            "AND resources.start_time < NOW()",
            "AND resources.valid_time > NOW()",
            "AND resources.end_time > NOW()",
            "AND role.role_is_delete = 0",
            "AND role.role_start_time < NOW()",
            "AND role.role_valid_time > NOW()",
            "AND role.role_end_time > NOW()",
            "AND role.role_id = #{roleId}"
    })
    List<ResourcesEntity> selectValidResourcesByRoleId(Role roleEntity);


    @Select({
            "<script>",
            "select resources.* from resources ",
            "JOIN resource_role_relation as rrr ON resources.resource_id = rrr.rrr_resource_id",
            "JOIN role ON role.role_id = rrr.rrr_role_id",
            "WHERE rrr.is_delete = 0",
            "AND rrr.rrr_valid_time <![CDATA[>]]> NOW()",
            "AND rrr.rrr_start_time <![CDATA[<]]> NOW()",
            "AND rrr.rrr_end_time <![CDATA[>]]> NOW()",
            "AND resources.is_delete = 0",
            "AND resources.start_time <![CDATA[<]]> NOW()",
            "AND resources.valid_time <![CDATA[>]]> NOW()",
            "AND resources.end_time <![CDATA[>]]> NOW()",
            "AND role.role_is_delete = 0",
            "AND role.role_start_time <![CDATA[<]]> NOW()",
            "AND role.role_valid_time <![CDATA[>]]> NOW()",
            "AND role.role_end_time <![CDATA[>]]> NOW()",
            "AND resource_role_relation.rrr_role_id IN (",
            "<foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.roleId}",
            "</foreach>",
            "</script>"
    })
    List<Resources> selectValidResourcesByRoleIds(List<Role> roleEntityList);

    @Select("select * from resources where resource_id = #{resourceId} and is_delete = 0 LIMIT 1")
    ResourcesEntity selectById(Resources resources);

    @Select("select * from resources where project_id = #{projectId} and is_delete = 0")
    ResourcesEntity selectByProjectId(Resources resources);

    @Select("select * from resources where module_id = #{moduleId} and is_delete = 0")
    ResourcesEntity selectByModuleId(Resources resources);

    @Update({
            "<script>",
            "update resources set is_delete = 1 ",
            "where resource_id in ",
            "<foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.resourceId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteResources(List<ResourcesEntity> resources);

    @Select("select * from resources where is_delete = 0")
    List<Resources> selectAllResources();

    @Select("select * from resources where is_delete = 0 and resource_update_time > #{time}")
    List<Resources> selectUpdatedResources(Resources resources);

    @Select({" select * from user_role_relation as urr inner join `role` as r on urr.role_id = r.role_id inner join resource_role_relation as rrr on r.role_id = rrr.rrr_role_id inner join resources as re on rrr.rrr_resource_id = re.resource_id where re.resource_name = #{resourceName}  ",
    "and urr.is_delete = 0 and r.is_delete = 0 and rrr.is_delete = 0 and re.is_delete = 0 and urr.valid_time > now() and urr.start_time < now() and urr.end_time > now() and r.valid_time > now() and r.start_time < now() and r.end_time > now() and rrr.valid_time > now() and rrr.start_time < now() and rrr.end_time > now() and re.valid_time > now() and re.start_time < now() and re.end_time > now()"
    })
    Resources authentication(AuthenticationData authenticationData);

    @Select({"select * from resources "})
    List<Resources> selectResources();

}
