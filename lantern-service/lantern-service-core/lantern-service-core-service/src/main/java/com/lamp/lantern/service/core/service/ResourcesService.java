package com.lamp.lantern.service.core.service;


import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.entity.RoleEntity;

import java.util.List;

 public interface ResourcesService {
   List<ResourcesEntity> selectByForm(ResourcesEntity resources);

   ResourcesEntity selectAllById(ResourcesEntity resources);

   ResourcesEntity selectAllByProjectId(ResourcesEntity resources);

   ResourcesEntity selectAllByModuleId(ResourcesEntity resources);

   Integer insertResource(ResourcesEntity resources);

   Integer insertResources(List<ResourcesEntity> resources);

   Integer updateByForm(ResourcesEntity resources);

   Integer deleteResource(ResourcesEntity resources);

   Integer deleteResources(List<ResourcesEntity> resources);

   List<ResourcesEntity> selectResourcesByRoleId(RoleEntity roleEntity);
 }

