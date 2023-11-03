package com.lamp.lantern.service.core.provider.mapper;

import com.lamp.lantern.service.core.entity.UserRoletypeRelationEntity;
import com.lamp.lantern.service.core.entity.database.UserRoletypeRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserRoletypeRelationMapper {

    @Insert("insert into `user_roletype_relation` (utr_user_id, utr_roletype_id,utr_valid_time) values (#{utrUserId}, #{utrRoletypeId},#{utrValidTime})")
    public Integer insertUserRoletypeRelation(UserRoletypeRelation userRoletypeRelationEntity);

//    @Update({"<script>",
//            "update `user_roletype_relation` set utr_valid_time = now() ",
//            "<where>",
//            "<if test = '#{utrUserId} != null'>utr_user_id=#{utrUserId},</if>",
//            "<if test = '#{utrRoletypeId} != null && #{utrUserId} != null'>or (utr_roletype_id=#{utrRoletypeId} and utr_user_id=#{utrUserId}),</if>",
//            "</where></script>"})
    @Update("update `user_roletype_relation` set utr_valid_time = now() where utr_user_id = #{utrUserId}")
    Integer endUserRoletpeRelation(UserRoletypeRelation userRoletypeRelationEntity);

    @Update({"<script>",
            "update `user_roletype_relation` set utr_valid_time = now() ",
            "where utr_user_id in ",
            "<foreach collection='userRoletypeRelationEntities' item='item' index='index' open='(' separator=',' close=')'>",
            "#{item.utrUserId}",
            "</foreach>",
            "</script>"
    })
    Integer endUserRoletpeRelations(List<UserRoletypeRelationEntity> userRoletypeRelationEntities);

    @Update("update `user_roletype_relation` set utr_valid_time = #{utrValidTime} where utr_id = #{utrId}")
    Integer updateUserRoletypeRelation(UserRoletypeRelation userRoletypeRelationEntity);
}
