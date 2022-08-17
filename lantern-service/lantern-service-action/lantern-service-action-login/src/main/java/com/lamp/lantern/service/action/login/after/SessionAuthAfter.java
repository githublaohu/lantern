package com.lamp.lantern.service.action.login.after;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

public class SessionAuthAfter implements AuthAfter{
  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {
    return false;
  }
}
