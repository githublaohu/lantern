package com.lamp.lantern.service.core.service;


import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.plugins.api.mode.Resources;

import java.util.List;

 public interface ResourcesService {
   List<Resources> selectByForm(ResourcesEntity resources);

   Resources selectAllById(ResourcesEntity resources);

   Resources selectAllByProjectId(ResourcesEntity  resources);

   Resources selectAllByModuleId(ResourcesEntity resources);

   Integer insertResource(ResourcesEntity resources);

   Integer insertResources(List<ResourcesEntity> resources);

   Integer updateByForm(ResourcesEntity resources);

   Integer deleteResource(ResourcesEntity resources);

   Integer deleteResources(List<ResourcesEntity> resources);

   List<Resources> selectResourcesByRoleId(RoleEntity roleEntity);

//   List<Resources> selectResourcesByUserId(UserInfoEntity userInfoEntity);
}
