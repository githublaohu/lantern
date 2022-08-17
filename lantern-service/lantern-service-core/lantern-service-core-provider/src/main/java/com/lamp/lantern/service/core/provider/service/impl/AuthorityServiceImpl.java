package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.service.core.entity.AuthorityEntity;
import com.lamp.lantern.service.core.provider.Mapper.AuthorityMapper;
import com.lamp.lantern.service.core.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

  @Autowired
  private AuthorityMapper authorityMapper;
  @Override
  public List<AuthorityEntity> selectByForm(AuthorityEntity authority) {
    return authorityMapper.selectByForm(authority);
  }

  @Override
  public Integer insertAuthority(AuthorityEntity authority) {
    return authorityMapper.insert(authority);
  }

  @Override
  public Integer updateAuthority(AuthorityEntity authority) {
    return authorityMapper.update(authority);
  }
}
