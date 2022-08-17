package com.lamp.lantern.service.action.login.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthOperateService {

  @Autowired
  private Map<String,AuthOperate> authOperateMap = new HashMap<>();


  public AuthOperate getAuthOperate(String name){

    return authOperateMap.get(name);
  }
}
