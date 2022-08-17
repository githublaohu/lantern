package com.lamp.lantern.service.action.login.auth;

import com.lamp.lantern.service.core.entity.UserInfoEntity;

import javax.servlet.ServletException;
import java.io.IOException;

public interface AuthOperate {

  public String manyWay();

  public String manufacturer();

  /**
   * 定义出参，入参
   * @return
   */
  public String auth() throws ServletException, IOException;

  /**
   * 是否需要调用手机号码，身份证
   * @return
   */
  public UserInfoEntity getUserInfoEntity();

}
