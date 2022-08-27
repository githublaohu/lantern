package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GatherPemissonAuthAfter implements AuthHandler {


  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {

    String uiToken = userInfoEntity.getUiToken();
    return false;
  }
}
