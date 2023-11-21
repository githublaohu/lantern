package com.lamp.lantern.service.action.login.controller;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.config.AuthChannelConfig;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.auth.qrcode.QrcodeAuthService;
import com.lamp.lantern.plugins.core.environment.SpringEnvironmentContext;
import com.lamp.lantern.plugins.core.login.HandlerExecute;
import com.lamp.lantern.plugins.core.login.HandlerService;
import com.lamp.lantern.plugins.core.login.config.HandlerConfig;
import com.lamp.lantern.plugins.core.login.config.LanternUserInfoConfig;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/account")
@RestController("loginController")
@Api(tags = {"账户管理"})
public class LoginController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private HandlerService handlerService;


    private LoginConfig loginConfig;

    @GetMapping("test")
    @ApiOperation(value = "测试")
    public String test() {
        return "test";
    }

    @PostConstruct
    private void init() throws Exception {

        AuthChannelConfig firstAuthChannelConfig = new AuthChannelConfig();
        firstAuthChannelConfig.setBeanName("lanternAuthOperate");
        List<AuthChannelConfig> authChannelConfigList = new ArrayList<AuthChannelConfig>();


        AuthChannelConfig thirdAuthChannelConfig = new AuthChannelConfig();

        thirdAuthChannelConfig.setSimpleClassName("GithubThirdAuthService");
        thirdAuthChannelConfig.setAppId("086907be34d757c8a85a");
        thirdAuthChannelConfig.setAccessKey("testAccessKey");
        thirdAuthChannelConfig.setSecretAccessKey("6957fd806ae26279a7b00c32c5262c50b24c0f80");
        thirdAuthChannelConfig.setPrivateKey("testPrivateKey");
        thirdAuthChannelConfig.setPublicKey("testPublicKey");
        thirdAuthChannelConfig.setRedirectUri("http://localhost:8083/lamp/lantern/service/action/account/third/redirect?authChannel=Github");

        AuthChannelConfig qrcodeAuthChannelConfig = new AuthChannelConfig();
        qrcodeAuthChannelConfig.setSimpleClassName("QrcodeAuthService");
        qrcodeAuthChannelConfig.setQrcodeExpire(600);
        qrcodeAuthChannelConfig.setRedisUrl("redis://localhost:6379/0");


        authChannelConfigList.add(firstAuthChannelConfig);
        authChannelConfigList.add(thirdAuthChannelConfig);
        authChannelConfigList.add(qrcodeAuthChannelConfig);

        //TODO 在这里加载Handler的配置
        List<HandlerConfig> handlerConfigList = new ArrayList<HandlerConfig>();

        //设置一个TokenConfig
        HandlerConfig tokenHandlerConfig = new HandlerConfig();
        tokenHandlerConfig.setHandlerName("CreateTokenAuthHandler");
        tokenHandlerConfig.setRedisName("testTokenRedis");

        //设置CreateTokenConfigMap
        HashMap<String, String> createTokenConfigMap = new HashMap<>();
        createTokenConfigMap.put("tokenName", "testTokenName");
        createTokenConfigMap.put("tokenCreateMode", "UUID");
        createTokenConfigMap.put("dataPosition", "cookie");
        createTokenConfigMap.put("cookieDomain", "testCookieDomain");

        tokenHandlerConfig.setConfigMap(createTokenConfigMap);

        handlerConfigList.add(tokenHandlerConfig);

        loginConfig = new LoginConfig();
        loginConfig.setSystemName("LanternAccount");
        loginConfig.setAuthChannelConfigList(authChannelConfigList);
        loginConfig.setHandlerConfigList(handlerConfigList);
        SpringEnvironmentContext springEvnironmentContext = new SpringEnvironmentContext(applicationContext);

        LanternUserInfoConfig lanternUserInfoConfig = new LanternUserInfoConfig();
        lanternUserInfoConfig.setBeanName("loginUserInfoService");
        loginConfig.setLanternUserInfoConfig(lanternUserInfoConfig);

        handlerService = new HandlerService();
        handlerService.setEnvironmentContext(springEvnironmentContext);

        //设置RedisMap
        Map<String, String> redisMap = new HashMap<>();
        redisMap.put("testTokenRedis", "redis://localhost:6379/0");
        redisMap.put("testTimesRedis", "redis://localhost:6379/0");
        redisMap.put("testWhiteListRedis", "redis://localhost:6379/0");
        redisMap.put("testExclusiveRedis", "redis://localhost:6379/0");
        handlerService.createConnection(redisMap);

        handlerService.createHandlerExecute(loginConfig, springEvnironmentContext);

    }

    @PostMapping("first/accountLogin")
    @ApiOperation(value = "账户密码登录")
    public ResultObject<String> accountLogin(@RequestBody UserInfoEntity userInfoEntity) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        return handlerExecute.execute(userInfoEntity);
    }

    @PostMapping("first/sendVerifyCode")
    @ApiOperation(value = "发送手机验证码")
    public ResultObject<String> sendVerifyCode(@RequestBody UserInfoEntity userInfoEntity) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        return handlerExecute.execute(userInfoEntity);
    }


    @ApiOperation(value = "获取第三方登录跳转uri")
    @GetMapping("third/oauthUrl")
    public RedirectView getRedirectUrl(@RequestParam String authChannel) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        String redirectUrl = handlerExecute.getRedirectUrl(authChannel);
        return new RedirectView(redirectUrl);
    }

    @GetMapping("third/redirect")
    @ApiOperation(value = "第三方登录重定向")
    public ResultObject<String> oauthLogin(@RequestParam String code, @RequestParam String authChannel) {
        UserInfo userInfoEntity = new UserInfoEntity();
        userInfoEntity.setToken(code);

        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        ResultObject<String> result = handlerExecute.execute(userInfoEntity, LoginType.THIRD, authChannel);
        if (result.getCode() == 200) {
            return result;
        } else {
            return result;
        }
    }

    @GetMapping("qrcode/getQrCode")
    @ApiOperation(value = "获取二维码")
    public QrcodeAuthService.QrInfo getQrCode() {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        QrcodeAuthService.QrInfo Qrcodes = handlerExecute.getQrcodeId();
        return Qrcodes;
    }

    @GetMapping("qrcode/checkQrCode")
    @ApiOperation(value = "检查二维码")
    public ResultObject<String> checkQrCode(@RequestParam  String code) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        return handlerExecute.checkQrcode(code);
    }

    @PostMapping("qrcode/scannedQrCode")
    @ApiOperation(value = "扫描二维码")
    public ResultObject<String> scannedQrCode(@RequestParam  String qrcodeid,String token) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        return handlerExecute.qrcodeMobileOperate(QrcodeAuthService.MobileOperate.Scan, qrcodeid,token);
    }

    @PostMapping("qrcode/confirmQrCode")
    @ApiOperation(value = "确认二维码")
    public ResultObject<String> confirmQrCode(@RequestParam  String qrcodeid,@RequestParam String token) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        return handlerExecute.qrcodeMobileOperate(QrcodeAuthService.MobileOperate.Auth, qrcodeid,token);
    }

    @PostMapping("qrcode/denyQrCode")
    @ApiOperation(value = "拒绝二维码")
    public ResultObject<String> denyQrCode(@RequestParam  String qrcodeid,@RequestParam String token) {
        HandlerExecute handlerExecute = handlerService.getHandlerExecute(loginConfig.getSystemName());
        return handlerExecute.qrcodeMobileOperate(QrcodeAuthService.MobileOperate.Deny, qrcodeid,token);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }



}
