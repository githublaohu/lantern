package com.lamp.lantern.service.core.service;


import com.lamp.lantern.service.core.entity.AuthorityEntity;

import java.util.List;

public interface AuthorityService {

  /**
   * 表单查询
   */
  public List<AuthorityEntity> selectByForm(AuthorityEntity authority);

  public Integer insertAuthority(AuthorityEntity authority);

  public Integer updateAuthority(AuthorityEntity authority);

}
