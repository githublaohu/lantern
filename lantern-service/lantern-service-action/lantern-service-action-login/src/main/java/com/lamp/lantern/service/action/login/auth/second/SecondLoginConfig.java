package com.lamp.lantern.service.action.login.auth.second;

import lombok.Data;

@Data
public class SecondLoginConfig {

  private String secondName;

  private String tokenLocal;

  private String tokenName;

  private String domain;

  private String secondAddress;

  private String requestMethod;

  private String secondTokenLocal;

  private String secondTokenName;

}
