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
 create_time    datetime     not null default current_timestamp comment ' 创建时间 ',
 start_time     datetime     not null default current_timestamp comment ' 开始时间 ',
 update_time    datetime     not null default current_timestamp on update current_timestamp comment ' 修改时间 ',
 end_time       datetime     not null default current_timestamp comment ' 结束时间 ',
 valid_time     datetime     not null default current_timestamp comment ' 有效时间 ',
 create_user_id bigint       not null comment '创建人id',
 update_user_id bigint       not null comment '修改人id',
 role_description varchar(127) not null default '' comment ' 角色描述 ',
 role_tag         varchar(127) not null default '' comment '',
 is_delete        int          not null default 0 comment '0未删除，1已删除'
 );
 */
@Mapper
public  interface RoleMapper {

    @Select({"<script>",
            "select * from role as r",
            "<where>",
            "<if test = 'roleId != null'>and role_id=#{roleId}</if>",
            "<if test = 'roleTypeId != null'>and role_type_id=#{roleTypeId}</if>",
            "<if test = 'systemId != null'>and system_id=#{systemId}</if>",
            "<if test = 'productId != null'>and product_id=#{productId}</if>",
            "<if test = 'projectId != null'>and project_id=#{projectId}</if>",
            "<if test = 'projectName != null'>and project_name=#{projectName}</if>",
            "<if test = 'roleType != null'>and role_type=#{roleType}</if>",
            "<if test = 'roleName != null'>and role_name=#{roleName}</if>",
            "<if test = 'createTime != null'>and create_time=#{createTime}</if>",
            "<if test = 'startTime != null'>and start_time=#{startTime}</if>",
            "<if test = 'updateTime != null'>and update_time=#{updateTime}</if>",
            "<if test = 'endTime != null'>and end_time=#{endTime}</if>",
            "<if test = 'validTime != null'>and valid_time=#{validTime}</if>",
            "<if test = 'createUserId != null'>and create_user_id=#{createUserId}</if>",
            "<if test = 'updateUserId != null'>and update_user_id=#{updateUserId}</if>",
            "<if test = 'roleDescription != null'>AND role_description LIKE CONCAT('%', #{roleDescription}, '%') </if>",
            "<if test = 'roleTag != null'>and role_tag=#{roleTag}</if>",
            "<if test = 'isDelete != null'>and is_delete=#{isDelete}</if>",
            "</where>",
            "</script>"

    })
     List<RoleEntity> queryByForm(Role roleEntity);

    @Update({"<script>",
            "update role",
            "<set>",
            "<if test = 'roleId != null'>role_id=#{roleId},</if>",
            "<if test = 'roleTypeId != null'>role_type_id=#{roleTypeId},</if>",
            "<if test = 'systemId != null'>system_id=#{systemId},</if>",
            "<if test = 'productId != null'>product_id=#{productId},</if>",
            "<if test = 'projectId != null'>project_id=#{projectId},</if>",
            "<if test = 'projectName != null'>project_name=#{projectName},</if>",
            "<if test = 'roleType != null'>role_type=#{roleType},</if>",
            "<if test = 'roleName != null'>role_name=#{roleName},</if>",
            "<if test = 'startTime != null'>start_time=#{startTime},</if>",
            "<if test = 'validTime != null'>valid_time=#{validTime},</if>",
            "<if test = 'roleDescription != null'>role_description=#{roleDescription},</if>",
            "<if test = 'roleTag != null'>role_tag=#{roleTag},</if>",
            "update_user_id = #{operatorId}",
            "</set>",
            "where role_id = #{roleId}",
            "</script>"
    })
     Integer updateRole(Role roleEntity);

    @Options(useGeneratedKeys = true, keyProperty = "roleId", keyColumn = "role_id")
    @Insert({
            "<script>",
            "insert into role (",
            "role_type_id,",
            "<if test='systemId != null'>system_id,</if>",
            "<if test='productId != null'>product_id,</if>",
            "<if test='projectId != null'>project_id,</if>",
            "<if test='projectName != null'>project_name,</if>",
            "<if test='roleType != null'>role_type,</if>",
            "<if test='roleName != null'>role_name,</if>",
            "<if test='roleDescription != null'>role_description,</if>",
            "<if test='roleTag != null'>role_tag,</if>",
            "start_time,valid_time,create_user_id,update_user_id",
            ") values (",
            "#{roleTypeId},",
            "<if test='systemId != null'>#{systemId},</if>",
            "<if test='productId != null'>#{productId},</if>",
            "<if test='projectId != null'>#{projectId},</if>",
            "<if test='projectName != null'>#{projectName},</if>",
            "<if test='roleType != null'>#{roleType},</if>",
            "<if test='roleName != null'>#{roleName},</if>",
            "<if test='roleDescription != null'>#{roleDescription},</if>",
            "<if test='roleTag != null'>#{roleTag},</if>",
            "#{startTime},#{validTime},#{operatorId},#{operatorId}",
            ")",
            "</script>"
    })
     Integer insertRole(Role roleEntity);

    @Select("SELECT CASE WHEN EXISTS (select 1 from role as r where role_id = #{roleId} and "+r_is_valid+") THEN 1 ELSE 0 END")
     Integer checkRoleValid(RoleEntity roleEntity) ;

    @Select("select * from role as r where "+r_is_valid)
     List<RoleEntity> getValidRoles();

    @Update("update role set end_time = now(),update_user_id=#{operatorId} where role_id = #{roleId}")
     Integer endRole(Role roleEntity);

    @Select({"select * from `role` as r where role_id in (",
            "select role_id from `user_role_relation` where user_id = #{uiId} and",
            urr_is_valid,")",
            "and",
            r_is_valid,}
    )
    List<RoleEntity> getAllRoleByUserId(UserInfo userInfoEntity);

    @Select({"select * from `role` as r inner join `user_role_relation` as urr on r.role_id = urr.role_id where",
    r_is_valid,"and",urr_is_valid,"and urr.user_id = #{uiId}"}
    )
    List<RoleEntity> getAllValidRoleByUserId(UserInfo userInfoEntity);

    @Update({
            "<script>",
            "update role set end_time = now() ",
            "where role_id in ",
            "<foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.roleId}",
            "</foreach>",
            "</script>"
    })
    Integer endRoles(List<RoleEntity> roleEntities);

    @Select(" select * from role as r where"+r_is_valid)
    List<RoleEntity> selectValidRoles();

    String r_is_valid = "r.start_time < now() and r.valid_time > now() and (r.end_time = r.create_time or r.end_time > now()) and r.is_delete = 0";
    String urr_is_valid = "urr.start_time < now() and urr.valid_time > now() and (urr .end_time = urr.create_time or urr.end_time > now()) and urr.is_delete = 0";

    @Select("select * from role where role_id = #{roleId} and is_delete = 0")
    RoleEntity queryByRoleId(RoleEntity roleEntity);
}
