package com.lamp.lantern.service.action.login.auth.first;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public abstract class AbstractFirstAuthOperate implements AuthOperate {

  private static final ThreadLocal<UserInfoEntity> LOCAL_USERINFO = new ThreadLocal<>();


  protected  void setLocalUserinfo(UserInfoEntity userInfoEntity){

    LOCAL_USERINFO.set(userInfoEntity);
  }


  public UserInfoEntity getUserInfoEntity(){

    return LOCAL_USERINFO.get();

  }
}
