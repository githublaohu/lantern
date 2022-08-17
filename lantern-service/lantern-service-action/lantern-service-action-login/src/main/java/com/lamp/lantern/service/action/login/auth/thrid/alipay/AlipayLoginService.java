package com.lamp.lantern.service.action.login.auth.thrid.alipay;
import com.lamp.lantern.service.core.entity.enums.BooleanEnum;
import com.lamp.lantern.service.core.entity.enums.StatusEnum;
import java.util.Date;
import com.lamp.lantern.service.core.entity.enums.GenderEnum;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.InitializingBean;

@Service
public class AlipayLoginService implements InitializingBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(AlipayLoginService.class);

  /**Alipay客户端*/
  private AlipayClient alipayClient;

  /**支付宝网关*/
  private static final String ALIPAY_BORDER_DEV = "https://openapi.alipaydev.com/gateway.do";
  private static final String ALIPAY_BORDER_PROD = "https://openapi.alipay.com/gateway.do";
  /**appID**/
  private static final String APP_ID_DEV = "xxxxxx";
  private static final String APP_ID_PROD = "xxxxxx";
  /**私钥*/
  private static final String APP_PRIVATE_KEY = "xxxxxx";
  /**公钥*/
  private static final String ALIPAY_PUBLIC_KEY = "xxxxx";

  @Override
  public void afterPropertiesSet() throws Exception {
    alipayClient = new DefaultAlipayClient(ALIPAY_BORDER_PROD, APP_ID_PROD, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
  }

  /**
   * 根据auth_code获取用户的user_id和access_token
   * @param authCode
   * @return
   */
  public String getAccessToken(String authCode) {
    AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
    request.setCode(authCode);
    request.setGrantType("authorization_code");
    try {
      AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
      return oauthTokenResponse.getAccessToken();
    } catch (Exception e) {

      LOGGER.error("使用authCode获取信息失败！", e);
      return null;
    }
  }

  /**
   * 根据access_token获取用户信息
   * @param token
   * @return
   */
  public UserInfoEntity getUserInfoByToken(String token) {
    AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest ();
    try {
      AlipayUserInfoShareResponse response =  alipayClient.execute(request, token);
      if (response.isSuccess()) {
        //打印响应信息
//                System.out.println(ReflectionToStringBuilder.toString(response));
        //封装支付宝对象信息
        UserInfoEntity userInfoEntity = new UserInfoEntity();


        return userInfoEntity;
      }
      LOGGER.error("根据 access_token获取用户信息失败!");
      return null;

    } catch (Exception e) {
      LOGGER.error("根据 access_token获取用户信息抛出异常！", e);
      return null;
    }
  }
}

