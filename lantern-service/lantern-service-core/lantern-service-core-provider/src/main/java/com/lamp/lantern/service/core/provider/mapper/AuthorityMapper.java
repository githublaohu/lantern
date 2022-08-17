package com.lamp.lantern.service.core.provider.Mapper;

import com.lamp.lantern.service.core.entity.AuthorityEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AuthorityMapper {


  @Select({"<script>",
      "select * from authority",
      "<where>",
      "<if test = 'id' != null>id=#{id},</if>",
      "<if test = 'authorityType' != null>authority_type=#{authorityType},</if>",
      "<if test = 'parentAuthorityId' != null>parent_authority_id=#{parentAuthorityId},</if>",
      "<if test = 'parentAuthorityName' != null>parent_authority_name=#{parentAuthorityName},</if>",
      "<if test = 'authorityName' != null>authority_name=#{authorityName},</if>",
      "<if test = 'authorityDesc' != null>authority_desc=#{authorityDesc},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "</where>",
      "</script>"

  })
  public List<AuthorityEntity> selectByForm(AuthorityEntity authority);

  @Update({"<script>",
      "update authority",
      "<set>",
      "<if test = 'authorityType' != null>authority_type=#{authorityType},</if>",
      "<if test = 'parentAuthorityId' != null>parent_authority_id=#{parentAuthorityId},</if>",
      "<if test = 'parentAuthorityName' != null>parent_authority_name=#{parentAuthorityName},</if>",
      "<if test = 'authorityName' != null>authority_name=#{authorityName},</if>",
      "<if test = 'authorityDesc' != null>authority_desc=#{authorityDesc},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "</set>",
      "where id = #{id}",
      "</script>"
  })
  public Integer update(AuthorityEntity authority);

  @Insert("insert into authority values(" +
      "#{id}, #{authorityType}, #{parentAuthorityId}, #{parentAuthorityName}," +
      " #{authorityName}, #{authorityDesc}, #{createTime}, " +
      "#{terminalTime}, #{roleDesc}, #{validTime}, #{status})")
  public Integer insert(AuthorityEntity authority);
}
