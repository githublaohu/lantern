package com.lamp.lantern.service.action.login.auth.first;

import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.UserInfoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component("first-lantern")
public class LanternAuthOperate extends AbstractFirstAuthOperate {

  private UserInfoService userInfoService;

  @Override
  public String manyWay() {
    return null;
  }

  @Override
  public String manufacturer() {
    return "lantern";
  }

  @Override
  public String auth() {

    return null;
  }

  public void partyLoginByUserName(){
    UserInfoEntity queryUserInfoEntity = userInfoService.checkUserExistByUserName(userInfoEntity);
    this.partyLonin(queryUserInfoEntity);
  }

  public void partyLoginByPhone(){
    UserInfoEntity queryUserInfoEntity = userInfoService.checkUserExistByPhone(userInfoEntity);
    this.partyLonin(queryUserInfoEntity);
  }

  public void partyLoginByEmail(){
    UserInfoEntity queryUserInfoEntity = userInfoService.checkUserExistByEmail(userInfoEntity);
    this.partyLonin(queryUserInfoEntity);
  }

  private void partyLonin(UserInfoEntity queryUserInfoEntity) {
    if(queryUserInfoEntity == null){
      resultObjectEnums = ResultObjectEnums.USERNAME_ERROR;
      return;
    }
    String uiSalt = queryUserInfoEntity.getUiSalt();
    String uiSaltPassword = DigestUtils.md5Hex(uiSalt + userInfoEntity.getUiPassword());
    if(Objects.equals(queryUserInfoEntity.getUiSaltPassword(), uiSaltPassword)){
      resultObjectEnums = ResultObjectEnums.SUCCESS;
      userInfoEntity = queryUserInfoEntity;
    }else{
      resultObjectEnums = ResultObjectEnums.PASSWORD_ERROR;
      increaseUserLoginError();
    }

    this.setLocalUserinfo(queryUserInfoEntity);
  }
}
