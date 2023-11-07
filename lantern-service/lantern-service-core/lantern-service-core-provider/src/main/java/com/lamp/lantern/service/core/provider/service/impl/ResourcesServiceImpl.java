package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.provider.mapper.ResourcesMapper;
import com.lamp.lantern.service.core.provider.mapper.RoleMapper;
import com.lamp.lantern.service.core.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ResourcesServiceImpl implements ResourcesService {
    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private RoleMapper roleMapper;

//    @PostConstruct
//    public void test() {
//        Resources resourcesEntity = new Resources();
//        resourcesEntity.setResourceId(1l);
//        resourcesEntity.setResourceSystemId(1l);
//        resourcesEntity.setResourceProjectId(1l);
//        resourcesEntity.setResourceProjectName("test");
//        resourcesEntity.setResourceModuleId(1l);
//        resourcesEntity.setResourceModuleName("test");
//        resourcesEntity.setResourceType("test");
//        resourcesEntity.setResourceName("test");
//        resourcesEntity.setResourceOperator("test");
//        resourcesEntity.setResourceConditions("test");
//        resourcesEntity.setResourceValidTime(LocalDateTime.now());
//        resourcesEntity.setResourceParentResourceId(1l);
//        resourcesEntity.setResourceDescription("test");
////        this.insert(resourcesEntity);
//        resourcesEntity.setResourceName("test2");
//        resourcesEntity.setResourceModuleName("test2");
//        resourcesEntity.setResourceType("test2");
////        this.update(resourcesEntity);
////        ResourcesEntity resourcesEntity1 = new ResourcesEntity();
////        resourcesEntity1.setResourceId(1l);
////        log.debug("selectById:{}", this.selectById(1l));
////        RoleEntity roleEntity = new RoleEntity();
////        roleEntity.setRoleId(1l);
////        List<ResourcesEntity> resourcesEntities = this.selectResourcesByRoleId(roleEntity);
//        UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUiId(1l);
//        List<Resources> res = this.selectResourcesByUserId(userInfoEntity);
//    }

    @Override
    public List<Resources> selectByForm(ResourcesEntity resources) {
        return resourcesMapper.selectByForm(resources);
    }

    @Override
    public Resources selectById(ResourcesEntity resources) {
        return resourcesMapper.selectById(resources);
    }

    @Override
    public Resources selectByProjectId(ResourcesEntity resources) {
        return resourcesMapper.selectByProjectId(resources);
    }

    @Override
    public Resources selectByModuleId(ResourcesEntity resources) {
        return resourcesMapper.selectByModuleId(resources);
    }

    @Override
    public Integer insertResource(ResourcesEntity resources) {
        return resourcesMapper.insert(resources);
    }

    @Override
    public Integer insertResources(List<ResourcesEntity> resources) {
        resources.forEach(this::insertResource);
        return 1;
    }

    @Override
    public Integer updateByForm(ResourcesEntity resources) {
        return resourcesMapper.updateByForm(resources);
    }

    @Override
    public Integer deleteResource(ResourcesEntity resources) {
        return resourcesMapper.delete(resources);
    }

    @Override
    public Integer deleteResources(List<ResourcesEntity> resources) {
        return resourcesMapper.deleteResources(resources);
    }

    @Override
    public List<Resources> selectResourcesByRoleId(RoleEntity roleEntity) {
        return resourcesMapper.selectValidResourcesByRoleId(roleEntity);
    }

    @Override
    public List<Resources> selectResourcesByUserId(UserInfoEntity userInfoEntity) {
        List<Role> roleEntities = roleMapper.getAllValidRoleByUserId(userInfoEntity);
        return resourcesMapper.selectValidResourcesByRoleIds(roleEntities);
    }

}
