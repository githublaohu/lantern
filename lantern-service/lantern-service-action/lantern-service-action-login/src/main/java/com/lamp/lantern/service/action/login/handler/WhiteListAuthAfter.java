package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.action.login.security.RedisService;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

import java.util.Objects;

import static com.lamp.lantern.service.core.entity.enums.StatusEnum.ACTIVE;


public class WhiteListAuthAfter extends AbstrackAuthHandler<WhiteListConfig> implements AuthHandler {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;


  @Override
  public boolean authBefore(UserInfoEntity userInfoEntity, AuthOperate authOperate) {
    Boolean isAuth = false;
    if(Objects.equals(config.getWhiteListSourceType() ,"entity" )){
      isAuth = userInfoEntity.getAllowLogin() == ACTIVE;
    }else {
      HashOperations<String, String, String> stringObjectObjectHashOperations = redisTemplate.opsForHash();
      String value = stringObjectObjectHashOperations.get("", "");
      if (Objects.equals(config.getWhiteListType(), "")) {
        isAuth = Objects.nonNull(value);
      } else {
        isAuth = Objects.isNull(value);
      }
    }
    if(!isAuth){
      // 打印日志
    }
    return isAuth;
  }
}
