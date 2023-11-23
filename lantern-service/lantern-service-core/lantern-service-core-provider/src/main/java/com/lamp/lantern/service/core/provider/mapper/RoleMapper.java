package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.plugins.api.mode.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select({"<script>",
            "select * from role",
            "<where>",
            "<if test = '#{roleId} != null'>role_id=#{roleId},</if>",
            "<if test = '#{roleName} != null'>role_name=#{roleName},</if>",
            "<if test = '#{roleCreateTime} != null'>role_create_time=#{roleCreateTime},</if>",
            "<if test = '#{roleEndTime} != null'>role_end_time=#{roleEndTime},</if>",
            "<if test = '#{roleUpdateTime} != null'>role_update_time=#{roleUpdateTime},</if>",
            "<if test = '#{roleValidTime} != null'>role_valid_time=#{roleValidTime},</if>",
            "<if test = '#{roleDescription} != null'>role_description=#{roleDescription},</if>",
            "<if test = '#{roleIsDelete} != null'>role_is_delete=#{roleIsDelete},</if>",
            "</where>",
            "</script>"

    })
    public List<Role> queryByForm(Role roleEntity);

    @Update({"<script>",
            "update resources",
            "<set>",
            "<if test = '#{roleId} != null'>role_id=#{roleId},</if>",
            "<if test = '#{roleName} != null'>role_name=#{roleName},</if>",
            "<if test = '#{roleDescription} != null'>role_description=#{roleDescription},</if>",
            "</set>",
            "where role_id = #{roldId}",
            "</script>"
    })
    public Integer updateRole(Role roleEntity);

    @Insert("insert into role (role_name, role_valid_time, role_description) values (#{roleName}, #{roleValidTime}, #{roleDescription})")
    public Integer insertRole(Role roleEntity);

    /**
     * 测试提供的roldId是否有效
     * @param id
     * @return
     */
    @Select("SELECT CASE WHEN EXISTS (select 1 from role where role_id = #{id} and role_valid_time > now()) THEN 1 ELSE 0 END")
    public Integer checkRoleValid(Long id) ;

    @Select("select * from role where role_valid_time > now()")
    public List<Role> getValidRoles();

    @Update("update role set role_end_time = now(),role_valid_time = now() where role_id = #{roleId}")
    public Integer endRole(Role roleEntity);

    @Select({"select * from `role` where role_id in (",
            "select urr_role_id from `user_role_relation` where urr_user_id = #{uiId} ",
            "union",
            " select trr_role_id from (roletype_role_relation join `user_roletype_relation` on trr_roletype_id = utr_roletype_id) where utr_user_id = #{uiId}) and role_is_delete = 0"}
    )
    List<Role> getAllRoleByUserId(UserInfo userInfoEntity);

    @Select({"select * from `role` where role_id in (",
    "select urr_role_id from `user_role_relation` where urr_user_id = #{uiId} and urr_valid_time > now() union select trr_role_id from (roletype_role_relation join `user_roletype_relation` on trr_roletype_id = utr_roletype_id and trr_valid_time > now() and utr_valid_time > now()) where utr_user_id = #{uiId}) and role_valid_time > now() and role_is_delete = 0"})
    List<Role> getAllValidRoleByUserId(UserInfo userInfoEntity);

    @Update({
            "<script>",
            "update role set role_valid_time = now() ",
            "role_end_time = now() ",
            "where role_id in ",
            "<foreach collection='roleEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.roleId}",
            "</foreach>",
            "</script>"
    })
    Integer endRoles(List<RoleEntity> roleEntities);

    @Select(" select * from role where role_is_delete = 0 and  role_end_time > now() ")
    List<Role>  selectRoles();
}
