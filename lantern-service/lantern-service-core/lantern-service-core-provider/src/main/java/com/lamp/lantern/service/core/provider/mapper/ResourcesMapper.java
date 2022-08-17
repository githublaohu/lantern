package com.lamp.lantern.service.core.provider.Mapper;

import com.lamp.lantern.service.core.entity.ResourcesEntity;
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
      "<if test = 'id' != null>id=#{id},</if>",
      "<if test = 'systemId' != null>system_id=#{systemId},</if>",
      "<if test = 'projectId' != null>project_id=#{projectId},</if>",
      "<if test = 'projectName' != null>project_name=#{projectName},</if>",
      "<if test = 'moduleId' != null>module_id=#{moduleId},</if>",
      "<if test = 'moduleName' != null>module_name=#{moduleName},</if>",
      "<if test = 'resourcesType' != null>resources_type=#{resourcesType},</if>",
      "<if test = 'resourcesName' != null>resources_name=#{resourcesName},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "<if test = 'operator' != null>operator=#{operator},</if>",
      "<if test = 'conditions' != null>conditions=#{conditions},</if>",
      "</where>",
      "</script>"

  })
  public List<ResourcesEntity> selectByForm(ResourcesEntity resources);

  @Update({"<script>",
      "update resources",
      "<set>",
      "<if test = 'systemId' != null>system_id=#{systemId},</if>",
      "<if test = 'projectId' != null>project_id=#{projectId},</if>",
      "<if test = 'projectName' != null>project_name=#{projectName},</if>",
      "<if test = 'moduleId' != null>module_id=#{moduleId},</if>",
      "<if test = 'moduleName' != null>module_name=#{moduleName},</if>",
      "<if test = 'resourcesType' != null>resources_type=#{resourcesType},</if>",
      "<if test = 'resourcesName' != null>resources_name=#{resourcesName},</if>",
      "<if test = 'createTime' != null>create_time=#{createTime},</if>",
      "<if test = 'terminalTime' != null>terminal_time=#{terminalTime},</if>",
      "<if test = 'roleDesc' != null>role_desc=#{roleDesc},</if>",
      "<if test = 'validTime' != null>valid_time=#{validTime},</if>",
      "<if test = 'status' != null>status=#{status},</if>",
      "<if test = 'operator' != null>operator=#{operator},</if>",
      "<if test = 'conditions' != null>conditions=#{conditions},</if>",
      "</set>",
      "where id = #{id}",
      "</script>"
  })
  public Integer update(ResourcesEntity resources);

  @Insert("insert into resources values( " +
      " #{id}, #{authorityType}, #{parentAuthorityId}, #{parentAuthorityName}, " +
      "#{authorityName}, #{authorityDesc}, #{createTime}, #{terminalTime}," +
      " #{roleDesc}, #{validTime}, #{status})")
  public Integer insert(ResourcesEntity resources);
}
