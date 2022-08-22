package com.lamp.lantern.service.action.login.handler;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthAfterService {

  private Map<String , AuthHandler> authAfterMap = new HashMap<>();

  private Map<String, List<AuthHandler>> authAfterChain = new ConcurrentHashMap<>();


  private Map<String,Object> map = new HashMap<>();

  @PostConstruct
  public void init(){

  }

  public List<AuthHandler> getAuthAfterChain(String authSystemName){

    return authAfterChain.get(authSystemName);
  }

}
