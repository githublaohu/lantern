package com.lamp.lantern.service.core.provider.Mapper;

import com.lamp.lantern.service.core.entity.AuthRoleRelationEntity;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AuthRoleRelationMapper {

  @Select({"<script>",
      "select * from resources",
      "<where>",

      "<if test = 'id' != null>id=#{id},</if>",
      "<if test = 'authorityId' != null>authority_id=#{authorityId},</if>",
      "<if test = 'operatorId' != null>operator_id=#{operatorId},</if>",
      "<if test = 'roleId' != null>role_id=#{roleId},</if>",
      "<if test = 'type' != null>type=#{type},</if>",
      "<if test = 'typeId' != null>type_id=#{typeId},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "</where>",
      "</script>"

  })
  public List<AuthRoleRelationEntity> queryRelation(AuthRoleRelationEntity authRoleRelation);
  @Update({"<script>",
      "update resources",
      "<set>",
      "<if test = 'id' != null>id=#{id},</if>",
      "<if test = 'authorityId' != null>authority_id=#{authorityId},</if>",
      "<if test = 'operatorId' != null>operator_id=#{operatorId},</if>",
      "<if test = 'roleId' != null>role_id=#{roleId},</if>",
      "<if test = 'type' != null>type=#{type},</if>",
      "<if test = 'typeId' != null>type_id=#{typeId},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "</set>",
      "where id = #{id}",
      "</script>"
  })
  public Integer updateRelation(AuthRoleRelationEntity authRoleRelation);


  @Insert("insert into authority_role_relation values( #{id}, #{authorityType}, #{parentAuthorityId}, #{parentAuthorityName}, #{authorityName}, #{authorityDesc}, #{createTime}, #{terminalTime}, #{roleDesc}, #{validTime}, #{status})")
  public Integer insertRelation(AuthRoleRelationEntity authRoleRelation);
}
