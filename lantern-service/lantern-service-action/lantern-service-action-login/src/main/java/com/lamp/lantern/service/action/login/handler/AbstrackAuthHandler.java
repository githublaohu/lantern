package com.lamp.lantern.service.action.login.handler;

public abstract class AbstrackAuthHandler<T> {

  protected T config;


  public void setConfig(T config){
    this.config = config;
  }


  public void init(){

  }

}
