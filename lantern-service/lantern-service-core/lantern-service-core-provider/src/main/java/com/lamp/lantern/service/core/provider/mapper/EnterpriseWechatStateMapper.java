package com.lamp.lantern.service.core.provider.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EnterpriseWechatStateMapper {

    @Insert("INSERT INTO lantern_oauth_state (system_id,state_hash,browser_hash,expires_at)"
            + " VALUES (#{systemId},#{stateHash},#{browserHash},TIMESTAMPADD(SECOND,#{ttl},NOW()))")
    int save(@Param("systemId") Long systemId, @Param("stateHash") String stateHash,
            @Param("browserHash") String browserHash, @Param("ttl") int ttl);

    @Delete("DELETE FROM lantern_oauth_state WHERE system_id=#{systemId} AND state_hash=#{stateHash}"
            + " AND browser_hash=#{browserHash} AND expires_at>NOW()")
    int consume(@Param("systemId") Long systemId, @Param("stateHash") String stateHash,
            @Param("browserHash") String browserHash);

    @Delete("DELETE FROM lantern_oauth_state WHERE system_id=#{systemId} AND expires_at<=NOW() LIMIT 1000")
    int removeExpired(@Param("systemId") Long systemId);
}
