package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.plugins.api.mode.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 (
 role_id          bigint(11) primary key auto_increment comment ' id ',
 role_type_id        bigint       not null comment ' 角色类型id ',
 system_id                   bigint       not null default 0 comment ' 系统id ',
 product_id                  bigint       not null default 0 comment '产品id',
 project_id                  bigint       not null default 0 comment ' 项目id ',
 project_name                varchar(127) not null default '' comment ' 项目名称 ',
 role_type varchar(15) not null default  '' comment  '角色类型',
 role_name        varchar(127) not null default '' comment ' 角色名 ',
 role_create_time    datetime     not null default current_timestamp comment ' 创建时间 ',
 role_start_time     datetime     not null default current_timestamp comment ' 开始时间 ',
 role_update_time    datetime     not null default current_timestamp on update current_timestamp comment ' 修改时间 ',
 role_end_time       datetime     not null default current_timestamp comment ' 结束时间 ',
 role_valid_time     datetime     not null default current_timestamp comment ' 有效时间 ',
 role_create_user_id bigint       not null comment '创建人id',
 role_update_user_id bigint       not null comment '修改人id',
 role_description varchar(127) not null default '' comment ' 角色描述 ',
 role_tag         varchar(127) not null default '' comment '',
 is_delete        int          not null default 0 comment '0未删除，1已删除'
 );
 */
@Mapper
public interface RoleMapper {

    @Select({"<script>",
            "select * from role",
            "<where>",
            "<if test = '#{roleId} != null'>role_id=#{roleId},</if>",
            "<if test = '#{roleTypeId} != null'>role_type_id=#{roleTypeId},</if>",
            "<if test = '#{systemId} != null'>system_id=#{systemId},</if>",
            "<if test = '#{productId} != null'>product_id=#{productId},</if>",
            "<if test = '#{projectId} != null'>project_id=#{projectId},</if>",
            "<if test = '#{projectName} != null'>project_name=#{projectName},</if>",
            "<if test = '#{roleType} != null'>role_type=#{roleType},</if>",
            "<if test = '#{roleName} != null'>role_name=#{roleName},</if>",
            "<if test = '#{roleCreateTime} != null'>role_create_time=#{roleCreateTime},</if>",
            "<if test = '#{roleStartTime} != null'>role_start_time=#{roleStartTime},</if>",
            "<if test = '#{roleUpdateTime} != null'>role_update_time=#{roleUpdateTime},</if>",
            "<if test = '#{roleEndTime} != null'>role_end_time=#{roleEndTime},</if>",
            "<if test = '#{roleValidTime} != null'>role_valid_time=#{roleValidTime},</if>",
            "<if test = '#{roleCreateUserId} != null'>role_create_user_id=#{roleCreateUserId},</if>",
            "<if test = '#{roleUpdateUserId} != null'>role_update_user_id=#{roleUpdateUserId},</if>",
            "<if test = '#{roleDescription} != null'>role_descriptio contains #{roleDescription},</if>",
            "<if test = '#{roleTag} != null'>role_tag=#{roleTag},</if>",
            "<if test = '#{isDelete} != null'>is_delete=#{isDelete},</if>",
            "</where>",
            "</script>"

    })
    public List<Role> queryByForm(Role roleEntity);

    @Update({"<script>",
            "update resources",
            "<set>",
            "<if test = '#{roleId} != null'>role_id=#{roleId},</if>",
            "<if test = '#{roleTypeId} != null'>role_type_id=#{roleTypeId},</if>",
            "<if test = '#{systemId} != null'>system_id=#{systemId},</if>",
            "<if test = '#{productId} != null'>product_id=#{productId},</if>",
            "<if test = '#{projectId} != null'>project_id=#{projectId},</if>",
            "<if test = '#{projectName} != null'>project_name=#{projectName},</if>",
            "<if test = '#{roleType} != null'>role_type=#{roleType},</if>",
            "<if test = '#{roleName} != null'>role_name=#{roleName},</if>",
            "<if test = '#{roleStartTime} != null'>role_start_time=#{roleStartTime},</if>",
            "<if test = '#{roleValidTime} != null'>role_valid_time=#{roleValidTime},</if>",
            "<if test = '#{roleDescription} != null'>role_description=#{roleDescription},</if>",
            "<if test = '#{roleTag} != null'>role_tag=#{roleTag},</if>",
            "roleUpdateUserId = #{roleUpdateUserId}",
            "</set>",
            "where role_id = #{roldId}",
            "</script>"
    })
    public Integer updateRole(Role roleEntity);

    @Options(useGeneratedKeys = true, keyProperty = "roleId", keyColumn = "role_id")
    @Insert("insert into role (role_type_id, system_id, product_id, project_id, project_name, role_type, role_name, role_start_time, role_valid_time, role_create_user_id, role_update_user_id, role_description, role_tag) values (#{roleTypeId}, #{systemId}, #{productId}, #{projectId}, #{projectName}, #{roleType}, #{roleName}, #{roleStartTime}, #{roleValidTime}, #{operatorId}, #{operatorId}, #{roleDescription}, #{roleTag})")
    public Integer insertRole(Role roleEntity);

    /**
     * 测试提供的roldId是否有效
     * @param id
     * @return
     */
    @Select("SELECT CASE WHEN EXISTS (select 1 from role where role_id = #{id} and "+r_is_valid+") THEN 1 ELSE 0 END")
    public Integer checkRoleValid(Long id) ;

    @Select("select * from role where "+r_is_valid)
    public List<Role> getValidRoles();

    @Update("update role set role_end_time = now(),role_update_user_id=#{operatorId} where role_id = #{roleId}")
    public Integer endRole(Role roleEntity);

    @Select({"select * from `role` where role_id in (",
            "select role_id from `user_role_relation` where user_id = #{uiId} and",
            urr_is_valid,")",
            "and",
            r_is_valid,}
    )
    List<Role> getAllRoleByUserId(UserInfo userInfoEntity);

    @Select({"select * from `role` as r inner join `user_role_relation` as urr on r.role_id = urr.role_id where",
    r_is_valid,"and",urr_is_valid,"and urr.user_id = #{uiId}"}
    )
    List<Role> getAllValidRoleByUserId(UserInfo userInfoEntity);

    @Update({
            "<script>",
            "update role set role_end_time = now() ",
            "where role_id in ",
            "<foreach collection='roleEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.roleId}",
            "</foreach>",
            "</script>"
    })
    Integer endRoles(List<RoleEntity> roleEntities);

    @Select(" select * from role where"+r_is_valid)
    List<Role> selectValidRoles();

    String r_is_valid = "role_start_time < now() and role_valid_time > now() and role_end_time > now() and role_is_delete = 0";
    String urr_is_valid = "urr_start_time < now() and urr_valid_time > now() and urr_end_time > now() and urr_is_delete = 0";
}
