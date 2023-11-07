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
//    @PostConstruct
//    void test(){
//        UserRoleRelationEntity userRoleRelationEntity = new UserRoleRelationEntity();
//        userRoleRelationEntity.setUrrUserId(1l);
//        userRoleRelationEntity.setUrrRoleId(1l);
//        userRoleRelationEntity.setUrrValidTime(LocalDateTime.now());
//        this.insertUserRoleRelation(userRoleRelationEntity);
//
//        UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUiId(1l);
//
//
//    }

    @Override
    public Integer insertUserRoleRelation(UserRoleRelationEntity userRoleRelationEntity) {
        return userRoleRelationMapper.insert(userRoleRelationEntity);
    }

    @Override
    public Integer insertUserRoleRelations(List<UserRoleRelationEntity> userRoleRelationEntities){
        userRoleRelationEntities.forEach(this::insertUserRoleRelation);
        return 1;
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


}
