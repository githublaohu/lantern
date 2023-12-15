package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.UserRoleRelationEntity;
import com.lamp.lantern.service.core.provider.mapper.UserRoleRelationMapper;
import com.lamp.lantern.service.core.service.UserRoleRelationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class UserRoleRelationServiceImpl implements UserRoleRelationService {

    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;


    @Override
    public Integer insertUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity) {
        userRoleRelationMapper.insertUserRoleRelation(userRoleRelationEntity);
        return userRoleRelationEntity.getUrrId().intValue();
    }


    @Override
    public Integer endUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity) {
        return userRoleRelationMapper.endUserRoleRelation(userRoleRelationEntity);
    }

    @Override
    public Integer endUserRoleRelations(List<UserRoleRelationEntity> userRoleRelationEntities) {
        return userRoleRelationMapper.endUserRoleRelations(userRoleRelationEntities);
    }

    @Override
    public Integer updateUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity) {
        return userRoleRelationMapper.updateUserRoleRelation(userRoleRelationEntity);
    }

    @Override
    public Integer deleteUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity) {
        return userRoleRelationMapper.deleteUserRoleRelation(userRoleRelationEntity);
    }


}
