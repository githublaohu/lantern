package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface AuthHandler {

  public default boolean authBefore(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return true;
  }

  public default boolean authAfter(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return true;
  }

  public default boolean errer(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return true;
  }

}
