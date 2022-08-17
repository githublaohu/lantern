package com.lamp.lantern.service.action.login.after;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.springframework.core.annotation.Order;

import java.util.Objects;

@Order
public class GetherUserInfoAuthAfter implements  AuthAfter{



  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {

    if(Objects.equals("",authOperate.manyWay())){
      UserInfoEntity newUserInfoEntity = authOperate.getUserInfoEntity();

      return true;
    }


    // 得到第三方表


    if( newUserInfoEntity != null){
      return true;
    }

    UserInfoEntity newUserInfoEntity = authOperate.getUserInfoEntity();

    if(true) {// 与用户关联
      // 通过手机号码，获得user信息
      // 如果没有就创建
      // user信息与第三方登录信息关联
      // 创建第三方登录信息
      // 查询userId 查询用户
    }else{
      // 创建userId
      // user信息与第三方登录信息关联
      // 创建第三方登录信息
      // 查询userId 查询用户
    }



    return false;
  }
}
