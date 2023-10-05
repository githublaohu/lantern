package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.ResourcesEntity;
import com.lamp.lantern.service.core.provider.mapper.ResourcesMapper;
import com.lamp.lantern.service.core.service.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResourcesServiceImpl implements ResourcesService {
  @Autowired
  private ResourcesMapper resourcesMapper;

  @Override
  public List<ResourcesEntity> selectByForm(ResourcesEntity resources) {
    return resourcesMapper.selectByForm(resources);
  }

  @Override
  public Integer insert(ResourcesEntity resources) {
    return resourcesMapper.insert(resources);
  }

  @Override
  public Integer update(ResourcesEntity resources) {
    return resourcesMapper.update(resources);
  }
}
