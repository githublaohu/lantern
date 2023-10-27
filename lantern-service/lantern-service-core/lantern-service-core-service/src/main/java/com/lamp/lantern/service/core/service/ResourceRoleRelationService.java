package com.lamp.lantern.service.core.service;


import com.lamp.lantern.service.core.entity.ResourceRoleRelationEntity;
import com.lamp.lantern.service.core.entity.database.ResourceRoleRelation;

import java.util.List;

public interface ResourceRoleRelationService {
    public Integer insertResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity);

    public Integer insertResourceRoleRelations(List<ResourceRoleRelationEntity> resourceRoleRelationEntities);
    public Integer updateResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity);
    public Integer endResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity);

    public Integer endResourceRoleRelations(List<ResourceRoleRelationEntity> resourceRoleRelationEntities);

    public Integer changeValidTime(ResourceRoleRelationEntity resourceRoleRelationEntity);
    public Integer deleteResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity);
    public List<ResourceRoleRelation> getResourceRoleRelationByResourceId(ResourceRoleRelationEntity resourceRoleRelationEntity);
    public List<ResourceRoleRelation> getResourceRoleRelationByRoleId(ResourceRoleRelationEntity resourceRoleRelationEntity);
    public List<ResourceRoleRelation> queryResourceRoleRelation(ResourceRoleRelationEntity resourceRoleRelationEntity);

    public Integer checkResourceRoleRelationValid(ResourceRoleRelationEntity resourceRoleRelationEntity);

    public List<ResourceRoleRelation> getValidResourceRoleRelation();

}
