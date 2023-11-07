package com.lamp.lantern.service.core.service;


import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.entity.RoleEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.plugins.api.mode.Resources;

import java.util.List;

public interface ResourcesService {
  public List<Resources> selectByForm(ResourcesEntity resources);

  public Resources selectById(ResourcesEntity resources);

  public Resources selectByProjectId(ResourcesEntity  resources);

  public Resources selectByModuleId(ResourcesEntity resources);

  public Integer insertResource(ResourcesEntity resources);

  public Integer insertResources(List<ResourcesEntity> resources);

  public Integer updateByForm(ResourcesEntity resources);

  public Integer deleteResource(ResourcesEntity resources);

  public Integer deleteResources(List<ResourcesEntity> resources);

  public List<Resources> selectResourcesByRoleId(RoleEntity roleEntity);

  public List<Resources> selectResourcesByUserId(UserInfoEntity userInfoEntity);
}
