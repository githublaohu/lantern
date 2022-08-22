package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public class BroadcastAuthAfter implements AuthHandler {
  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {
    
    return false;
  }
}
