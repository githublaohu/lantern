package com.lamp.lantern.service.action.login.after;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthAfterService {

  private Map<String , AuthAfter> authAfterMap = new HashMap<>();

  private Map<String, List<AuthAfter>> authAfterChain = new ConcurrentHashMap<>();


  private Map<String,Object> map = new HashMap<>();

  @PostConstruct
  public void init(){

  }

  public List<AuthAfter> getAuthAfterChain(String authSystemName){

    return authAfterChain.get(authSystemName);
  }

}
