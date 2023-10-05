package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;

public interface AuthHandler {

  public default ResultObject<String> authBefore(UserInfo userInfo){
    return null;
  }

  public default ResultObject<String> authAfter(UserInfo userInfo){
    return null;
  }

  public default ResultObject<String> error(UserInfo userInfo ){
    return null;
  }

}
