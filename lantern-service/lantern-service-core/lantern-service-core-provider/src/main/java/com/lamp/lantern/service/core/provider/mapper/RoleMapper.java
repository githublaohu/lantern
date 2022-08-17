package com.lamp.lantern.service.core.provider.Mapper;

import com.lamp.lantern.service.core.entity.RoleEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoleMapper  {

  @Select({"<script>",
      "select * from authority",
      "<where>",
      "<if test = 'id' != null>id=#{id},</if>",
      "<if test = 'roleName' != null>role_name=#{roleName},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "</where>",
      "</script>"

  })
  public List<RoleEntity> queryByForm(RoleEntity roleEntity);
  @Update({"<script>",
      "update resources",
      "<set>",
      "<if test = 'id' != null>id=#{id},</if>",
      "<if test = 'roleName' != null>role_name=#{roleName},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "</set>",
      "where id = #{id}",
      "</script>"
  })
  public Integer updateRole(RoleEntity roleEntity);

  @Insert("insert into role values(#{id}, #{authorityType}, #{parentAuthorityId}, #{parentAuthorityName}, #{authorityName}, #{authorityDesc}, #{createTime}, #{terminalTime}, #{roleDesc}, #{validTime}, #{status})")
  public Integer insertRole(RoleEntity roleEntity);
}
