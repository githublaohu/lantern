package com.lamp.lantern.plugins.auth.second;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.service.action.login.auth.first.AbstractFirstAuthOperate;


@ConfigurationProperties("SecondConfig")
@Component("second-lantern")
public class SecondAuthOperate extends AbstractFirstAuthOperate {

  private Map<String,SecondLoginConfig> secondLoginConfigMap = new HashMap<>();

  /**
   * 认证用户的token
   * @return
   */
  @Override
  public ResultObject<Object> auth(UserInfo userInfo)  {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    // 第一步获得域名
    String domain = request.getHeader("Host");
    // 找第二方呀
    SecondLoginConfig secondLoginConfig = this.secondLoginConfigMap.get(domain);
    if(Objects.isNull(secondLoginConfig)){
      return null;
    }
    String token = null;
    if(Objects.equals("header",secondLoginConfig.getTokenLocal())){
      token = request.getHeader(secondLoginConfig.getTokenName());

    }else if (Objects.equals("cooke", secondLoginConfig.getTokenLocal())){
      Cookie[] cookies = request.getCookies();
      for(Cookie cookie : cookies) {
    	  if(Objects.equals(cookie.getName(), secondLoginConfig.getTokenName())) {
    		  token = cookie.getName();
    	  }
      }
    }else if (Objects.equals("path",secondLoginConfig.getTokenLocal())){
      request.getRequestURI();
    }
    if(Objects.isNull(token)){
      //重定向
      HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
      response.sendRedirect(secondLoginConfig.getSecondAddress());
      String s = "";
    }
    //
    if(request.isRequestedSessionIdValid()){
      return null;
    }
    // 使用ligth 调用二方接口，要求二方解决返回用户数据
    
    
    
    return null;
  }


}
