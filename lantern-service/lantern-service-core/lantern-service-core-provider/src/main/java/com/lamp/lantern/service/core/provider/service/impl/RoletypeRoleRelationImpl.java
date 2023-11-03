package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.RoletypeRoleRelationEntity;
import com.lamp.lantern.service.core.entity.database.RoletypeRoleRelation;
import com.lamp.lantern.service.core.provider.mapper.RoletypeRoleRelationMapper;
import com.lamp.lantern.service.core.service.RoletypeRoleRelationService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RoletypeRoleRelationImpl implements RoletypeRoleRelationService {
    @Autowired
    private RoletypeRoleRelationMapper roletypeRoleRelationMapper;


    @PostConstruct
    void test(){
        RoletypeRoleRelationEntity roletypeRoleRelationEntity = new RoletypeRoleRelationEntity();
        roletypeRoleRelationEntity.setTrrRoleId(1l);
        roletypeRoleRelationEntity.setTrrRoletypeId(1l);
        roletypeRoleRelationEntity.setTrrValidTime(LocalDateTime.now());
        this.insertRoletypeRoleRelation(roletypeRoleRelationEntity);
    }
    @Override
    public Integer insertRoletypeRoleRelation(RoletypeRoleRelationEntity roletypeRoleRelationEntity) {
        return roletypeRoleRelationMapper.insertRoletypeRoleRelation(roletypeRoleRelationEntity);
    }

    @Override
    public Integer insertRoletypeRoleRelations(List<RoletypeRoleRelationEntity> roletypeRoleRelationEntities) {
        roletypeRoleRelationEntities.forEach(this::insertRoletypeRoleRelation);
        return 1;
    }

    @Override
    public Integer endRoletpeRoleRelation(RoletypeRoleRelationEntity roletypeRoleRelationEntity) {
        return roletypeRoleRelationMapper.endRoletpeRoleRelation(roletypeRoleRelationEntity);
    }

    @Override
    public Integer endRoletpeRoleRelations(List<RoletypeRoleRelationEntity> roletypeRoleRelationEntities) {
        return roletypeRoleRelationMapper.endRoletpeRoleRelations(roletypeRoleRelationEntities);
    }

    @Override
    public Integer updateRoletypeRoleRelation(RoletypeRoleRelationEntity roletypeRoleRelationEntity) {
        return roletypeRoleRelationMapper.updateRoletypeRoleRelation(roletypeRoleRelationEntity);
    }
}
