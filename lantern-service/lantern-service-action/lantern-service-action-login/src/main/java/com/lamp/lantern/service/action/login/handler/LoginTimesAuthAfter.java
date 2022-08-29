package com.lamp.lantern.service.action.login.handler;

import com.lamp.lantern.service.action.login.auth.AuthOperate;
import com.lamp.lantern.service.action.login.handler.config.LoginTimesConfig;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginTimesAuthAfter extends AbstrackAuthHandler<LoginTimesConfig> implements AuthHandler {

  private static final ThreadLocal<LoginTimesInfo> TIMES_LOCAL = new ThreadLocal<LoginTimesInfo>(){
    protected LoginTimesInfo initialValue() {
      return new LoginTimesInfo();
    }
  };

  @Autowired
  private RedisTemplate<String, Integer> redisTemplate;

  private ValueOperations<String, Integer> stringStringValueOperations;

  public void init(){
    stringStringValueOperations = redisTemplate.opsForValue();
  }

  public boolean authBefore(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    LoginTimesInfo loginTimesInfo = TIMES_LOCAL.get();
    loginTimesInfo.clean();

    if(config.getAddressTimeLong() > 0){
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      request.getRemoteAddr();
      String key = "";
      loginTimesInfo.setAddressLimesKey(key);
      Integer integer = stringStringValueOperations.get(key);
      loginTimesInfo.setAddressLimes(Objects.isNull(integer) ? -1 : integer);
    }if(config.getTimes() > 0){
      String key  = "";
      loginTimesInfo.setTimesKey(key);
      Integer integer = stringStringValueOperations.get(key);
      loginTimesInfo.setTimes(Objects.isNull(integer) ? -1 : integer);
    }
    if( loginTimesInfo.getTimes() >= config.getTimes() || loginTimesInfo.getAddressLimes() >= config.getAddressLimes()){

      return true;
    }else {
      return false;

    }
  }

  @Override
  public boolean authAfter(UserInfoEntity userInfoEntity, AuthOperate authOperate) {
    LoginTimesInfo loginTimesInfo = TIMES_LOCAL.get();
    if(Objects.nonNull(loginTimesInfo)){
      if( loginTimesInfo.getTimes() != -1){
        redisTemplate.delete(loginTimesInfo.getTimesKey());
      }
      if(loginTimesInfo.getAddressLimes() != -1){
        redisTemplate.delete(loginTimesInfo.getAddressLimesKey());
      }
    }
    return false;
  }

  public boolean errer(UserInfoEntity userInfoEntity , AuthOperate authOperate){
    LoginTimesInfo loginTimesInfo = TIMES_LOCAL.get();

    if(config.getTimes() > 0){
      redisTemplate.expire(loginTimesInfo.getTimesKey(),);
      if(loginTimesInfo.getTimes() <= config.getTimes()){
        stringStringValueOperations.increment(loginTimesInfo.getTimesKey());
      }
    }if(config.getAddressLimes() > 0 ){

      if(loginTimesInfo.getAddressLimes() <= config.getAddressLimes()){
        stringStringValueOperations.increment(loginTimesInfo.getTimesKey());
      }
      if(!Objects.equals(TimeUnit.DAYS ,config.getAddressTimeUnit())) {
        redisTemplate.expire(loginTimesInfo.getTimesKey(), );
      }else{
        if(loginTimesInfo.getAddressLimes() == -1){
          redisTemplate.expire(loginTimesInfo.getTimesKey(), );
        }
      }
    }
    return true;
  }


  @Data
  static class  LoginTimesInfo{

    private Integer times;

    private Integer addressLimes;

    private String timesKey;

    private String addressLimesKey;

    public void clean(){
      this.addressLimes = -1;
      this.addressLimesKey = null;
      this.times = -1;
      this.timesKey = null;
    }

  }
}
