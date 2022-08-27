package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface AuthHandler {

  public default boolean authBefore(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    //如果token为null，直接FALSE
    if (userInfoEntity.getUiToken() != null) {




    } else {
      return false;
    }

    return true;
  }

  public default boolean authAfter(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return true;
  }

  public default boolean error(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return true;
  }

}
