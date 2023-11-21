package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.RoletypeRoleRelationEntity;
import com.lamp.lantern.plugins.api.mode.RoletypeRoleRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoletypeRoleRelationMapper {


    @Insert("insert into `roletype_role_relation` (trr_role_id, trr_roletype_id,trr_valid_time) values (#{trrRoleId}, #{trrRoletypeId},#{trrValidTime})")
    public Integer insertRoletypeRoleRelation(RoletypeRoleRelation roletypeRoleRelationEntity);

//    @Update({"<script>",
//            "update `roletype_role_relation` set trr_valid_time = now() ",
//            "<where>",
//                "<if test = '#{trrRoleId} != null'>trr_role_id=#{trrRoleId}</if>",
//                "<if test = '#{trrRoletypeId} != null && #{trrRoleId} != null'>or (trr_roletype_id=#{trrRoletypeId} and trr_role_id= #{trrRoleId})</if>",
//            "</where></script>"})
    @Update("update `roletype_role_relation` set trr_valid_time = now(),trr_end_time = now() where trr_role_id = #{trrRoleId}")
    Integer endRoletpeRoleRelation(RoletypeRoleRelation roletypeRoleRelationEntity);

    @Update("update `roletype_role_relation` set trr_valid_time = #{trrValidTime} where trr_role_id = #{trrRoleId}")
    Integer updateRoletypeRoleRelation(RoletypeRoleRelation roletypeRoleRelationEntity);

    @Update({
            "<script>",
            "update `roletype_role_relation` set trr_valid_time = now(),trr_end_time = now() ",
            "where trr_role_id in ",
            "<foreach collection='roletypeRoleRelationEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.trrRoleId}",
            "</foreach>",
            "</script>"
    })
    Integer endRoletpeRoleRelations(List<RoletypeRoleRelationEntity> roletypeRoleRelationEntities);
}
