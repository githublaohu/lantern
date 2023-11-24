package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.RoleTypeEntity;
import com.lamp.lantern.plugins.api.mode.RoleType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoleTypeMapper {

    @Insert("insert into `role_type` (role_type_name) values ( #{roleTypeName})")
    public Integer insertRoleType(RoleType roleTypeEntity);


    @Update("update `role_type` set role_type_is_delete = 1 where role_type_id = #{roleTypeId}")
    Integer deleteRoleType(RoleType roleTypeEntity);

    @Update({
            "<script>",
            "update `role_type` set role_type_is_delete = 1 ",
            "where role_type_id in ",
            "<foreach collection='roleTypeEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.roleTypeId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteRoleTypes(List<RoleTypeEntity> roleTypeEntities);
}
