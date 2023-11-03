package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.RoletypeEntity;
import com.lamp.lantern.service.core.entity.database.Roletype;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoletypeMapper {

    @Insert("insert into `roletype` (roletype_name) values ( #{roletypeName})")
    public Integer insertRoletype(Roletype roletypeEntity);


    @Update("update `roletype` set roletype_is_delete = 1 where roletype_id = #{roletypeId}")
    Integer deleteRoletype(Roletype roletypeEntity);

    @Update({
            "<script>",
            "update `roletype` set roletype_is_delete = 1 ",
            "where roletype_id in ",
            "<foreach collection='roletypeEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.roletypeId}",
            "</foreach>",
            "</script>"
    })
    Integer deleteRoletypes(List<RoletypeEntity> roletypeEntities);
}
