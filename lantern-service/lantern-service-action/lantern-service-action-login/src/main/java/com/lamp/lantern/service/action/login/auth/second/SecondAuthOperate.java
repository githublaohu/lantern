package com.lamp.lantern.service.action.login.auth.second;

import com.lamp.lantern.service.action.login.auth.first.AbstractFirstAuthOperate;
import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.catalina.connector.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


@ConfigurationProperties("SecondConfig")
@Component("second-lantern")
public class SecondAuthOperate extends AbstractFirstAuthOperate {

  private Map<String,SecondLoginConfig> secondLoginConfigMap = new HashMap<>();

  @Override
  public String manyWay() {
    return null;
  }

  @Override
  public String manufacturer() {
    return "lantern";
  }

  /**
   * 认证用户的token
   * @return
   */
  @Override
  public String auth() throws ServletException, IOException {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    // 第一步获得域名

    String domain = request.getHeader("");
    domain.replace("")

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
    }else if (Objects.equals("path",secondLoginConfig.getTokenLocal())){
      request.getRequestURI();
    }

    if(Objects.isNull(token)){
      //重定向

      ServletResponse Response = null;
      request.getRequestDispatcher("path").forward(request, Response);
      String s = "";
    }
    //
    if(request.isRequestedSessionIdValid()){

      return null;
    }


    return token;
  }


}
