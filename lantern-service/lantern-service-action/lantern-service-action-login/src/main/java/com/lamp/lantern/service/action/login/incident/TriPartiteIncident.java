package com.lamp.lantern.service.action.login.incident;


import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.service.action.login.function.ConstantWxUtils;
import com.lamp.lantern.service.action.login.security.Decript;
import com.lamp.lantern.service.action.login.security.RedisService;
import com.lamp.lantern.service.action.login.utils.HttpClientUtils;
import com.lamp.lantern.service.action.login.utils.HttpUtils;
import com.lamp.lantern.service.action.login.utils.ParseHttpRequest;
import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.LoginRecordEntity;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.entity.enums.GenderEnum;
import com.lamp.lantern.service.core.entity.enums.LoginPatternEnum;
import com.lamp.lantern.service.core.service.LoginRecordEntityService;
import com.lamp.lantern.service.core.service.UserInfoEntityService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Builder
public class TriPartiteIncident {

    private RedisService redisService;

    private UserInfoEntityService userInfoEntityService;

    private UserInfoEntity userInfoEntity;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ResultObjectEnums resultObjectEnums;

    private ConstantWxUtils constantWxUtils;

    private JSONObject loginJsonObject;

    private LoginRecordEntity loginRecordEntity;

    private LoginPatternEnum loginPatternEnum;

    private LoginRecordEntityService loginRecordEntityService;

    public ResultObjectEnums getResultObjectEnums(){
        return resultObjectEnums;
    }

    public void callback() throws IOException {
        System.out.println("callback");
        request.setCharacterEncoding("UTF-8");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;


        //排序
        String sortString = HttpUtils.sort("jaycase", timestamp, nonce);
        //加密
        String mytoken = Decript.SHA1(sortString);
        //校验签名
        if (mytoken != null && mytoken != "" && mytoken.equals(signature)) {
            System.out.println("验证成功");
            response.reset();
            response.setContentType(echostr);
            out = response.getWriter();
            out.write(echostr);
            out.flush();
            out.close();
            resultObjectEnums = ResultObjectEnums.SUCCESS;

        } else {
            resultObjectEnums = ResultObjectEnums.WECHAT_SIGNATURE_ERROR;
        }
    }

    public void redirect(){
        try{
            String baseUrl = constantWxUtils.getBaseUrl();
            String redirect_url = URLEncoder.encode(constantWxUtils.getRedirectUrl(), "utf-8");
            String url = String.format(
                    baseUrl,
                    constantWxUtils.getAppId(),
                    redirect_url
            );
            response.sendRedirect(url);
            System.out.println("redirect " + url);
            resultObjectEnums = ResultObjectEnums.SUCCESS;


        } catch (Exception e) {
            e.printStackTrace();
            resultObjectEnums = ResultObjectEnums.WECHAT_REDIRECT_FAIL;
        }
    }

    public void checkLoginStatus(){

        if(loginJsonObject == null || loginJsonObject.containsKey("erroecode")){
            resultObjectEnums = ResultObjectEnums.WECHAT_SCAN_LOGIN_ERROR;
            if(loginJsonObject != null){
                resultObjectEnums.setResultObjectMessage(loginJsonObject.getString("errmsg"));
            }
        }


    }

    public void insertUserLoginRecord(){

        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        System.out.println("after 2 " + userInfoEntity);

        loginRecordEntity.setUiId(userInfoEntity.getUiId());
        loginRecordEntity.setTriId(userInfoEntity.getTriId());
        loginRecordEntity.setUlLoginAddress("");
        loginRecordEntity.setUlLoginIp("");
        loginRecordEntity.setUlLoginDevice(ParseHttpRequest.getDeviceEnum(request));
        loginRecordEntity.setUlLoginTerminal(ParseHttpRequest.getTerminalEnum(request));
        loginRecordEntity.setUlLoginDeviceModel(ParseHttpRequest.getDeviceModel(request));
        loginRecordEntity.setUlLoginSystem(ParseHttpRequest.getLoginSystem(request));
        //loginRecordEntity.setUlLoginWay();

        System.out.println("loginRecordEntity " + loginRecordEntity);

        loginRecordEntityService.insertLoginRecord(loginRecordEntity);
    }

    public void login() throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String code = request.getParameter("code");
        String status = request.getParameter("status");

        String authUrl = String.format(
                constantWxUtils.getAuthUrl(),
                constantWxUtils.getAppId(),
                constantWxUtils.getAppSecret(),
                code
        );



        loginJsonObject = HttpUtils.httpGet(authUrl);

        System.out.println(loginJsonObject);

        System.out.println(resultObjectEnums);

        checkLoginStatus();
        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        String userInfoUrl = String.format(
                constantWxUtils.getInfoUrl(),
                loginJsonObject.getString("access_token"),
                loginJsonObject.getString("openid")
        );



        loginJsonObject = HttpUtils.httpGet(userInfoUrl);

        checkLoginStatus();
        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }

        loginByJsonObject();

    }

    public void loginByJsonObject(){
        if(!Objects.equals(resultObjectEnums, ResultObjectEnums.SUCCESS)){
            return;
        }
        UserInfoEntity queryUserInfoEntity = new UserInfoEntity();
        queryUserInfoEntity.setUiIdcard(getUiIdcard());
        queryUserInfoEntity = userInfoEntityService.checkUserExistByIdcard(queryUserInfoEntity);

        System.out.println("queryUserInfoEntity "+ queryUserInfoEntity);

        if(Objects.equals(queryUserInfoEntity, null)){
            registerByWxScan();
        }else{
            userInfoEntity = queryUserInfoEntity;
        }

        insertUserLoginRecord();

    }

    public void registerByWxScan(){

        UserInfoEntity registerUserInfoEntity = new UserInfoEntity();
        registerUserInfoEntity.setUiName(getUiName());
        registerUserInfoEntity.setUiNickname(getUiNickName());
        registerUserInfoEntity.setUiIdcard(getUiIdcard());
        registerUserInfoEntity.setUiPhone(getUiPhone());
        registerUserInfoEntity.setUiEmail(getUiEmail());
        registerUserInfoEntity.setUiHeadPortrait(getUiHeadPortrait());
        registerUserInfoEntity.setUiLackFlag(getUiLackFlag());
        registerUserInfoEntity.setUiSex(getUiSex());
        registerUserInfoEntity.setUiAge(getUiAge());
        registerUserInfoEntity.setUiBirth(getUiBirth());
        registerUserInfoEntity.setUiAddress(getUiAddress());
        registerUserInfoEntity.setUiLoginAddress(getUiLoginAddress());
        registerUserInfoEntity.setUiPassword(getUiPasword());
        registerUserInfoEntity.setUiSalt(getUiSalt());
        registerUserInfoEntity.setUiSaltPassword(getUiSaltPassword());
        registerUserInfoEntity.setUiToken(getUiToken());

        userInfoEntityService.registerUserInfoEntity(registerUserInfoEntity);

        System.out.println("注册成功");

        userInfoEntity = registerUserInfoEntity;

        System.out.println("userInfoEntity " + userInfoEntity);

    }

    public String getUiName(){
        return loginJsonObject.getString("nickname");
    }

    public String getUiNickName(){
        return loginJsonObject.getString("nickname");
    }

    public String getUiIdcard(){
        return loginJsonObject.getString("openid");
    }

    public Date getUiBirth(){
        return new Date(2100,1,1);
    }

    public String getUiPhone(){
        // 测试接口为最基本接口，暂未实现该功能，后续补充
        return "";
    }

    public String getUiEmail(){
        return "";
    }

    public String getUiHeadPortrait(){
        System.out.println("len () " + loginJsonObject.getString("headimgurl").length());
        return loginJsonObject.getString("headimgurl");
    }

    public Integer getUiLackFlag(){
        return 0;
    }

    public GenderEnum getUiSex(){
        if(loginJsonObject.getInteger("sex") == 0){
            return GenderEnum.MALE;
        }else if(loginJsonObject.getInteger("sex") == 1){
            return GenderEnum.FAMALE;
        }else{
            return GenderEnum.UNKNOWN;
        }
    }

    public Integer getUiAge(){
        return -1;
    }


    public String getUiAddress(){
        return "";
    }

    public String getUiLoginAddress(){
        return loginJsonObject.getString("country") + loginJsonObject.getString("province")
                + loginJsonObject.getString("city");
    }

    public String getUiPasword(){
        return "";
    }

    public String getUiSaltPassword(){
        return "";

    }

    public String getUiSalt(){
        return "";
    }

    public String getUiToken(){return "";}













}
