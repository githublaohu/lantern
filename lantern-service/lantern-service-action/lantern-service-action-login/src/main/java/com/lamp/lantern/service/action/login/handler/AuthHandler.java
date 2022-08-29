package com.lamp.lantern.service.action.login.handler;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public interface AuthHandler {

  public default ResultObject<Object> authBefore(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return null;
  }

  public default ResultObject<Object> authAfter(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return null;
  }

  public default ResultObject<Object> errer(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    return null;
  }

}
