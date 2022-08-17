package com.lamp.lantern.service.action.login.after;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;

import static com.lamp.lantern.service.core.entity.enums.StatusEnum.ACTIVE;


public class WhiteListAuthAfter implements  AuthAfter{
  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {
    if(userInfoEntity.getAllowLogin() == ACTIVE){
      return true;
    }
    return false;
  }
}
