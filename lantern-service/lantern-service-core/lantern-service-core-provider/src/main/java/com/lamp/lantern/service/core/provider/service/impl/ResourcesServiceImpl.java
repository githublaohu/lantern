package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.entity.RoleEntity;
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

    @PostConstruct
    public void test() {
        ResourcesEntity resourcesEntity = new ResourcesEntity();
        resourcesEntity.setResourceId(1l);
        resourcesEntity.setSystemId(1l);
        resourcesEntity.setProjectId(1l);
        resourcesEntity.setProjectName("test");
        resourcesEntity.setModuleId(1l);
        resourcesEntity.setModuleName("test");
        resourcesEntity.setResourceType("test");
        resourcesEntity.setResourceName("test");
        resourcesEntity.setResourceOperate("test");
        resourcesEntity.setResourceConditions("test");
        resourcesEntity.setValidTime(LocalDateTime.now());
        resourcesEntity.setResourceParentResourceId(1l);
        resourcesEntity.setResourceDescription("test");
//        this.insert(resourcesEntity);
        resourcesEntity.setResourceName("test2");
        resourcesEntity.setModuleName("test2");
        resourcesEntity.setResourceType("test2");
        resourcesEntity.setProductId(1L);
        resourcesEntity.setOperatorId(1L);
        resourcesEntity.setValidTime(LocalDateTime.now());
        resourcesEntity.setStartTime(LocalDateTime.now());
        resourcesEntity.setResourceVersion("1L");
        this.insertResource(resourcesEntity);

//        this.update(resourcesEntity);
//        ResourcesEntity resourcesEntity1 = new ResourcesEntity();
//        resourcesEntity1.setResourceId(1l);
//        log.debug("selectById:{}", this.selectById(1l));
//        RoleEntity roleEntity = new RoleEntity();
//        roleEntity.setRoleId(1l);
//        List<ResourcesEntity> resourcesEntities = this.selectResourcesByRoleId(roleEntity);
//        UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUiId(1l);
//        List<Resources> res = this.selectResourceId(userInfoEntity);
//        System.out.println(res);
    }

    @Override
    public List<ResourcesEntity> selectByForm(ResourcesEntity resources) {
        return resourcesMapper.selectByForm(resources);
    }

    @Override
    public ResourcesEntity selectAllById(ResourcesEntity resources) {
        return resourcesMapper.selectById(resources);
    }

    @Override
    public ResourcesEntity selectAllByProjectId(ResourcesEntity resources) {
        return resourcesMapper.selectByProjectId(resources);
    }

    @Override
    public ResourcesEntity selectAllByModuleId(ResourcesEntity resources) {
        return resourcesMapper.selectByModuleId(resources);
    }

    @Override
    public Integer insertResource(ResourcesEntity resources) {
        resourcesMapper.insert(resources);
        return resources.getResourceId().intValue();
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
        return resourcesMapper.deleteResourceById(resources);
    }

    @Override
    public Integer deleteResources(List<ResourcesEntity> resources) {
        return resourcesMapper.deleteResources(resources);
    }

    @Override
    public List<ResourcesEntity> selectResourcesByRoleId(RoleEntity roleEntity) {
        return resourcesMapper.selectValidResourcesByRoleId(roleEntity);
    }


}
