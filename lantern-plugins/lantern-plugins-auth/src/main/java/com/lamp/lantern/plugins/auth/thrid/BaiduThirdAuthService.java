package com.lamp.lantern.plugins.auth.thrid;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractThirdAuthService;

import java.text.MessageFormat;

@AuthTypeChannel(loginType = LoginType.THIRD, authChannel = "Baidu")
public class BaiduThirdAuthService extends AbstractThirdAuthService {

//    private BaiduPushClient baiduPushClient;
    @Override
    public AuthResultObject auth(UserInfo userInfo) {
        AuthResultObject authResultObject = AuthResultObject.create();
        return authResultObject;
    }

    @Override
    public AuthResultObject getUserInfo(UserInfo userInfo) {
        AuthResultObject authResultObject = AuthResultObject.create();
        return authResultObject;
    }

    @Override
    public void doInitialization() {
        // TODO Auto-generated method stub

    }

	/**
	 * 请求用户授权时百度提供了一个在OAuth2.0协议中没有提到的参数：display。它是用来标识不同形式的客户端所对应的不同展现形式的授权页面,其值定义如下：
	 *
	 * page：全屏形式的授权页面(默认)，适用于web应用。
	 * popup: 弹框形式的授权页面，适用于桌面软件应用和web应用。
	 * dialog:浮层形式的授权页面，只能用于站内web应用。
	 * mobile: Iphone/Android等智能移动终端上用的授权页面，适用于Iphone/Android等智能移动终端上的应用。
	 * tv: 电视等超大显示屏使用的授权页面。
	 * pad: IPad/Android等智能平板电脑使用的授权页面。
	 * @return
	 */
    @Override
    public RedirectAddress getRedirectAddress() {
        return getRedirectAddress("page");
    }

	public RedirectAddress getRedirectAddress(String display) {
		String url = "http://openapi.baidu.com/oauth/2.0/authorize?response_type=code&client_id={0}&redirect_uri={1}&scope=email&display={2}";
		url = MessageFormat.format(url, config.getAppId(), config.getRedirectUri(),display );
		return RedirectAddress.create(url);
	}
}
