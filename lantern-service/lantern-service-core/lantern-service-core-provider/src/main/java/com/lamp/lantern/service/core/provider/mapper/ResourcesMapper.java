package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

//
//create table resources
//(
//    resource_id                 bigint primary key auto_increment comment ' id ',
//    system_id                   bigint       not null default 0 comment ' 系统id ',
//    product_id                  bigint       not null default 0 comment '产品id',
//    project_id                  bigint       not null default 0 comment ' 项目id ',
//    project_name                varchar(127) not null default '' comment ' 项目名称 ',
//    module_id                   bigint       not null default 0 comment ' 模块id ',
//    module_name                 varchar(127) not null default '' comment ' 模块名称 ',
//    resource_type               varchar(127) not null default '' comment ' 资源类型 ',
//    resource_version            varchar(31)  not null default '' comment '资源版本号',
//    resource_name               varchar(127) not null default '' comment ' 资源名 ',
//    resource_create_time        datetime     not null default current_timestamp comment ' 创建时间 ',
//    resource_start_time         datetime     not null default current_timestamp comment '权限开始时间',
//    resource_valid_time         datetime     not null default current_timestamp comment ' 有效时间 ',
//    resource_end_time           datetime     not null default current_timestamp comment '权限结束时间，自动与手动触发',
//    resource_update_time        datetime     not null default current_timestamp on update current_timestamp comment ' 修改时间 ',
//    resource_create_user_id     bigint       not null comment '创建人id',
//    resource_update_user_id     bigint       not null comment '修改人id',
//    resource_description        varchar(255) not null default '' comment '资源描述',
//    resource_tag                varchar(255) not null default '' comment '',
//    resource_operator           varchar(127) not null default '' comment ' 操作 ',
//    resource_conditions         varchar(127) not null default '' comment ' 条件，视图触发条件为主 ',
//    resource_parent_resource_id bigint       not null default 0 comment ' 父权限id ',
//    is_delete                   int          not null default 0 comment '0未删除，1已删除'
//);
@Mapper
public interface ResourcesMapper {

    @Select({"<script>",
            "select * from resources",
            "<where>",
            "<if test = '#{resourceId} != null'>resource_id=#{resourceId},</if>",
            "<if test = '#{systemId} != null'>system_id=#{systemId},</if>",
            "<if test = '#{projectId} != null'>resource_project_id=#{projectId},</if>",
            "<if test = '#{projectName} != null'>project_id=#{projectName},</if>",
            "<if test = '#{moduleId} != null'>module_id=#{moduleId},</if>",
            "<if test = '#{moduleName} != null'>module_name=#{moduleName},</if>",
            "<if test = '#{resourceType} != null'>resource_type=#{resourceType},</if>",
            "<if test = '#{resourceVersion} != null'>resource_version=#{resourceVersion},</if>",
            "<if test = '#{resourceName} != null'>resource_name=#{resourceName},</if>",
            "<if test = '#{resourceCreateTime} != null'>resource_create_time=#{resourceCreateTime},</if>",
            "<if test = '#{resourceStartTime} != null'>resource_start_time=#{resourcestartTime},</if>",
            "<if test = '#{resourceValidTime} != null'>resource_valid_time=#{resourceValidTime},</if>",
            "<if test = '#{resourceEndTime} != null'>resource_end_time=#{resourceEndTime},</if>",
            "<if test = '#{resourceUpdateTime} != null'>resource_update_time=#{resourceUpdateTime},</if>",
            "<if test = '#{resourceCreateUserId} != null'>resource_create_user_id=#{resourceCreateUserId},</if>",
            "<if test = '#{resourceUpdateUserId} != null'>resource_update_user_id=#{resourceUpdateUserId},</if>",
            "<if test = '#{resourceDescription} != null'>resource_description=#{resourceDescription},</if>",
            "<if test = '#{resourceTag} != null'>resource_tag=#{resourceTag},</if>",
            "<if test = '#{resourceOperator} != null'>resource_operator=#{resourceOperator},</if>",
            "<if test = '#{resourceConditions} != null'>resource_conditions=#{resourceConditions},</if>",
            "<if test = '#{resourceParentResourceId} != null'>resource_parent_resource_id=#{resourceParentResourceId},</if>",
            "<if test = '#{isDelete} != null'>is_delete=#{isDelete},</if>",
            "</where>",
            "</script>"

    })
    public List<Resources> selectByForm(Resources resources);

    @Update({"<script>",
            "update resources",
            "<set>",
            "<if test = '#{systemId} != null'>system_id=#{systemId},</if>",
            "<if test = '#{projectId} != null'>resource_project_id=#{projectId},</if>",
            "<if test = '#{projectName} != null'>project_id=#{projectName},</if>",
            "<if test = '#{moduleId} != null'>module_id=#{moduleId},</if>",
            "<if test = '#{moduleName} != null'>module_name=#{moduleName},</if>",
            "<if test = '#{resourceType} != null'>resource_type=#{resourceType},</if>",
            "<if test = '#{resourceVersion} != null'>resource_version=#{resourceVersion},</if>",
            "<if test = '#{resourceName} != null'>resource_name=#{resourceName},</if>",
            "<if test = '#{resourceStartTime} != null'>resource_start_time=#{resourcestartTime},</if>",
            "<if test = '#{resourceValidTime} != null'>resource_valid_time=#{resourceValidTime},</if>",
            "<if test = '#{resourceEndTime} != null'>resource_end_time=#{resourceEndTime},</if>",
            "<if test = '#{resourceDescription} != null'>resource_description=#{resourceDescription},</if>",
            "<if test = '#{resourceTag} != null'>resource_tag=#{resourceTag},</if>",
            "<if test = '#{resourceOperator} != null'>resource_operator=#{resourceOperator},</if>",
            "<if test = '#{resourceConditions} != null'>resource_conditions=#{resourceConditions},</if>",
            "<if test = '#{resourceParentResourceId} != null'>resource_parent_resource_id=#{resourceParentResourceId},</if>",
            "<if test = '#{isDelete} != null'>is_delete=#{isDelete},</if>",
            "resource_update_user_id=#{operatorId},",
            "</set>",
            "where resource_id = #{resourceId}",
            "</script>"
    })
    public Integer updateByForm(Resources resources);

    @Options(useGeneratedKeys = true, keyProperty = "resourceId", keyColumn = "resource_id")
    @Insert("insert into resources(system_id,product_id,project_id,project_name,module_id,module_name,resource_type,resource_version,resource_name,resource_start_time,resource_valid_time,resource_create_user_id,resource_update_user_id,resource_description,resource_tag,resource_operator,resource_conditions,resource_parent_resource_id) values (#{systemId},#{productId},#{projectId},#{projectName},#{moduleId},#{moduleName},#{resourceType},#{resourceVersion},#{resourceName},#{resourceStartTime},#{resourceValidTime},#{operatorId},#{operatorId},#{resourceDescription},#{resourceTag},#{resourceOperator},#{resourceConditions},#{resourceParentResourceId})")
    public Integer insert(Resources resources);

    @Update("update resources set is_delete = 1,resource_update_user_id=#{operatorId} where resource_id = #{resourceId}")
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
            "AND resources.resource_start_time < NOW()",
            "AND resources.resource_valid_time > NOW()",
            "AND resources.resource_end_time > NOW()",
            "AND role.role_is_delete = 0",
            "AND role.role_start_time < NOW()",
            "AND role.role_valid_time > NOW()",
            "AND role.role_end_time > NOW()",
            "AND role.role_id = #{roleId}"
    })
    List<Resources> selectValidResourcesByRoleId(Role roleEntity);


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
            "AND resources.resource_start_time <![CDATA[<]]> NOW()",
            "AND resources.resource_valid_time <![CDATA[>]]> NOW()",
            "AND resources.resource_end_time <![CDATA[>]]> NOW()",
            "AND role.role_is_delete = 0",
            "AND role.role_start_time <![CDATA[<]]> NOW()",
            "AND role.role_valid_time <![CDATA[>]]> NOW()",
            "AND role.role_end_time <![CDATA[>]]> NOW()",
            "AND resource_role_relation.rrr_role_id IN (",
            "<foreach collection='roleEntityList' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.rrrRoleId}",
            "</foreach>",
            "</script>"
    })
    List<Resources> selectValidResourcesByRoleIds(List<Role> roleEntityList);

    @Select("select * from resources where resource_id = #{resourceId} and is_delete = 0 LIMIT 1")
    Resources selectById(Resources resources);

    @Select("select * from resources where project_id = #{projectId} and is_delete = 0")
    Resources selectByProjectId(Resources resources);

    @Select("select * from resources where module_id = #{moduleId} and is_delete = 0")
    Resources selectByModuleId(Resources resources);

    @Update({
            "<script>",
            "update resources set is_delete = 1 ",
            "where resource_id in ",
            "<foreach collection='resources' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.resourceId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteResources(List<ResourcesEntity> resources);

    @Select("select * from resources where is_delete = 0")
    List<Resources> selectAllResources();

    @Select("select * from resources where is_delete = 0 and resource_update_time > #{time}")
    List<Resources> selectUpdatedResources(Resources resources);

    @Select({" select * from user_role_relation as urr inner join `role` as r on urr.role_id = r.role_id inner join resource_role_relation as rrr on r.role_id = rrr.rrr_role_id inner join resources as re on rrr.rrr_resource_id = re.resource_id where re.resource_name = #{resourceName}  "})
    Resources authentication(AuthenticationData authenticationData);

    @Select({"select * from resources "})
    List<Resources> selectResources();

}
