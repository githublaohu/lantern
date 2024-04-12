package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.ResourceRoleRelationEntity;
import com.lamp.lantern.plugins.api.mode.ResourceRoleRelation;
import com.lamp.lantern.service.core.provider.mapper.ResourceRoleRelationMapper;
import com.lamp.lantern.service.core.service.ResourceRoleRelationService;
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
public class ResourceRoleRelationServiceImpl implements ResourceRoleRelationService {

    @Autowired
    private ResourceRoleRelationMapper resourceRoleRelationMapper;

    @PostConstruct
    public void init() {
        ResourceRoleRelationEntity resourceRoleRelationEntity = new ResourceRoleRelationEntity();
        resourceRoleRelationEntity.setResourceId(1l);
        resourceRoleRelationEntity.setRoleId(1l);
        resourceRoleRelationEntity.setOperatorId(1l);
        resourceRoleRelationEntity.setRrrType("test");
        resourceRoleRelationEntity.setRrrTypeId(1l);
        resourceRoleRelationEntity.setValidTime(LocalDateTime.now());
        resourceRoleRelationEntity.setStartTime(LocalDateTime.now());
        int a = this.insertResourceRoleRelation(resourceRoleRelationEntity);
        log.info("insertResourceRoleRelation:{}", a);
    }

    @Override
    public Integer insertResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        return resourceRoleRelationMapper.insertResourceRoleRelation(resourceRoleRelationEntity);
    }

    @Override
    public Integer updateResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        Integer result = resourceRoleRelationMapper.updateResourceRoleRelation(resourceRoleRelationEntity);
        return result;
    }

    @Override
    public Integer endResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        resourceRoleRelationEntity.setEndTime(LocalDateTime.now());
        resourceRoleRelationEntity.setValidTime(LocalDateTime.now());
        Integer result = resourceRoleRelationMapper.updateResourceRoleRelation(resourceRoleRelationEntity);
        return result;
    }

    @Override
    public Integer endResourceRoleRelations(List<ResourceRoleRelationEntity> resourceRoleRelationEntities) {
        return resourceRoleRelationMapper.endResourceRoleRelations(resourceRoleRelationEntities);
    }

    @Override
    public Integer changeValidTime(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        Integer result = resourceRoleRelationMapper.changeValidTime(resourceRoleRelationEntity);
        return result;
    }

    @Override
    public Integer deleteResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        return resourceRoleRelationMapper.deleteResourceRoleRelation(resourceRoleRelationEntity);
    }

    @Override
    public List<ResourceRoleRelation> getResourceRoleRelationByResourceId(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        List<ResourceRoleRelation> result = resourceRoleRelationMapper.getResourceRoleRelationByResourceId(resourceRoleRelationEntity);
        return result;}

    @Override
    public List<ResourceRoleRelation> getResourceRoleRelationByRoleId(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        List<ResourceRoleRelation> result = resourceRoleRelationMapper.getResourceRoleRelationByRoleId(resourceRoleRelationEntity);
        return result;}

    @Override
    public List<ResourceRoleRelation> queryResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        List<ResourceRoleRelation> result = resourceRoleRelationMapper.queryResourceRoleRelation(resourceRoleRelationEntity);
        return result;}

    /**
     * 0 表示有效
     */
    @Override
    public Integer checkResourceRoleRelationValid(ResourceRoleRelationEntity resourceRoleRelationEntity) {
        ResourceRoleRelation resourceRoleRelationEntity1 = new ResourceRoleRelation();
        resourceRoleRelationEntity1.setRrrId(resourceRoleRelationEntity.getRrrId());
        List<ResourceRoleRelation> resourceRoleRelationEntities = resourceRoleRelationMapper.queryResourceRoleRelation(resourceRoleRelationEntity1);
        if (resourceRoleRelationEntities.size() == 0) {
            return 1;
        }
        ResourceRoleRelation resourceRoleRelationEntity2 = resourceRoleRelationEntities.get(0);
        if (resourceRoleRelationEntity2.getValidTime().isBefore(LocalDateTime.now()) && resourceRoleRelationEntity2.getEndTime().isAfter(LocalDateTime.now())) {
            return 0;
        }
        return 1;
    }

    @Override
    public List<ResourceRoleRelation> getValidResourceRoleRelation() {
        return  resourceRoleRelationMapper.getValidResourceRoleRelation();
    }


}
