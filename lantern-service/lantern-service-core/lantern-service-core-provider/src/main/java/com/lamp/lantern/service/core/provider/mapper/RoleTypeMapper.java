package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.RoleTypeEntity;
import com.lamp.lantern.plugins.api.mode.RoleType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoleTypeMapper {

    @Options(useGeneratedKeys = true, keyProperty = "roleTypeId", keyColumn = "role_type_id")
    @Insert("insert into `role_type` (role_type_name,role_type_description) values ( #{roleTypeName},#{roleTypeDescription})")
    Integer insertRoleType(RoleType roleTypeEntity);


    @Update("update `role_type` set is_delete = 1 where role_type_id = #{roleTypeId}")
    Integer deleteRoleType(RoleType roleTypeEntity);

    @Update({
            "<script>",
            "update `role_type` set is_delete = 1 ",
            "where role_type_id in ",
            "<foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.roleTypeId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteRoleTypes(List<RoleTypeEntity> roleTypeEntities);
}
