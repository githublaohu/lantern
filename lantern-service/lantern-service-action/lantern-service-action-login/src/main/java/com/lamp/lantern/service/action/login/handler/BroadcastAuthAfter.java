package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.enums.StatusEnum;

public class BroadcastAuthAfter implements AuthHandler {
  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {
    //从用户实体类获取
    StatusEnum allowLogin = userInfoEntity.getAllowLogin();
    String auth = authOperate.auth("");
    return false;
  }
}
