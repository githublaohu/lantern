package com.lamp.lantern.service.core.service;


import com.lamp.lantern.service.core.entity.ResourcesEntity;

import java.util.List;

public interface ResourcesService {
  public List<ResourcesEntity> selectByForm(ResourcesEntity resources);

  public Integer insert(ResourcesEntity resources);

  public Integer update(ResourcesEntity resources);
}
