package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CteateTockenAuthAfter implements AuthHandler {

  private TokenConfig tokenConfig;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private ValueOperations<String, String> stringStringValueOperations;

  public void init(){
    stringStringValueOperations = redisTemplate.opsForValue();
  }

  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {
    String token = null;

    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    Cookie cookie = new Cookie(tokenConfig.getTokenName(),token);
    response.addCookie(cookie);

    response.addHeader(tokenConfig.getTokenName(),token);

    stringStringValueOperations.set();


    return false;

  }
}
