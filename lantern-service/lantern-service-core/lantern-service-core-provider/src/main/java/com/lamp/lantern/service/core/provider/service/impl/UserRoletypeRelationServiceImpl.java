package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.UserRoletypeRelationEntity;
import com.lamp.lantern.service.core.entity.database.UserRoletypeRelation;
import com.lamp.lantern.service.core.provider.mapper.UserRoletypeRelationMapper;
import com.lamp.lantern.service.core.service.UserRoletypeRelationService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserRoletypeRelationServiceImpl implements UserRoletypeRelationService {

    @Autowired
    private UserRoletypeRelationMapper userRoletypeRelationMapper;

    @PostConstruct
    void test(){
        UserRoletypeRelationEntity roletypeRoleRelationEntity = new UserRoletypeRelationEntity();
        roletypeRoleRelationEntity.setUtrUserId(1l);
        roletypeRoleRelationEntity.setUtrRoletypeId(1l);
        roletypeRoleRelationEntity.setUtrValidTime(LocalDateTime.now());

        this.insertUserRoletypeRelation(roletypeRoleRelationEntity);
    }

    @Override
    public Integer insertUserRoletypeRelation(UserRoletypeRelationEntity userRoletypeRelationEntity) {
        return userRoletypeRelationMapper.insertUserRoletypeRelation(userRoletypeRelationEntity);
    }

    @Override
    public Integer insertUserRoletypeRelations(List<UserRoletypeRelationEntity> userRoletypeRelationEntities) {
        userRoletypeRelationEntities.forEach(this::insertUserRoletypeRelation);
        return 1;
    }

    @Override
    public Integer endUserRoletpeRelation(UserRoletypeRelationEntity userRoletypeRelationEntity) {
        return userRoletypeRelationMapper.endUserRoletpeRelation(userRoletypeRelationEntity);
    }

    @Override
    public Integer endUserRoletpeRelations(List<UserRoletypeRelationEntity> userRoletypeRelationEntities) {
        return userRoletypeRelationMapper.endUserRoletpeRelations(userRoletypeRelationEntities);
    }

    @Override
    public Integer updateUserRoletypeRelation(UserRoletypeRelationEntity userRoletypeRelationEntity) {
        return userRoletypeRelationMapper.updateUserRoletypeRelation(userRoletypeRelationEntity);
    }
}
