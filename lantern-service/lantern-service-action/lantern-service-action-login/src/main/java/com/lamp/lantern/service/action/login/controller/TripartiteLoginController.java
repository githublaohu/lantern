package com.lamp.lantern.service.action.login.controller;


import com.google.gson.Gson;
import com.lamp.lantern.service.action.login.function.ConstantWxUtils;
import com.lamp.lantern.service.action.login.incident.IncidentService;
import com.lamp.lantern.service.action.login.incident.LoginIncident;
import com.lamp.lantern.service.action.login.incident.TriPartiteIncident;
import com.lamp.lantern.service.action.login.security.Decript;
import com.lamp.lantern.service.action.login.utils.HttpClientUtils;
import com.lamp.lantern.service.action.login.utils.JsonData;
import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;
import com.lamp.lantern.service.core.entity.UserInfoEntity;
import com.lamp.lantern.service.core.service.UserInfoEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import com.lamp.lantern.service.action.login.utils.ResultObjectEnums;

@Slf4j
@RequestMapping("/api/ucenter/wx")
@RestController("/api/ucenter/wx")
@Api(tags = {"三方登录"})
public class TripartiteLoginController {

    @Autowired
    private ConstantWxUtils constantWxUtils;

    @Autowired
    private IncidentService incidentService;

    // 测试接口 测试微信小程序是否联通
    @RequestMapping("/callback")
    @ApiOperation(value = "微信小程序ping")
    public void callback(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        TriPartiteIncident.TriPartiteIncidentBuilder builder = incidentService.createTriPartiteIncidentBuilder();
        builder.request(request);
        builder.response(response);
        builder.constantWxUtils(constantWxUtils);

        TriPartiteIncident incident = builder.build();
        incident.callback();


    }

    @RequestMapping("/testRedirect")
    public ResultObjectEnums testRedirect(HttpServletRequest request, HttpServletResponse response){
        return ResultObjectEnums.SUCCESS;
    }

    @RequestMapping("/redirect")
    @ApiOperation(value = "跳转扫码页面")
    public ResultObjectEnums redirect(HttpServletRequest request, HttpServletResponse response){
        TriPartiteIncident.TriPartiteIncidentBuilder builder = incidentService.createTriPartiteIncidentBuilder();
        builder.request(request);
        builder.response(response);
        builder.constantWxUtils(constantWxUtils);

        TriPartiteIncident incident = builder.build();
        incident.redirect();
        return incident.getResultObjectEnums();


    }

    @RequestMapping("/login")
    @ApiOperation(value = "微信扫码登录")
    public ResultObjectEnums login(HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println(request);
        System.out.println(response);
        TriPartiteIncident.TriPartiteIncidentBuilder builder = incidentService.createTriPartiteIncidentBuilder();
        builder.request(request);
        builder.response(response);
        builder.constantWxUtils(constantWxUtils);

        TriPartiteIncident incident = builder.build();
        incident.login();
        return incident.getResultObjectEnums();
    }





}





/*
*
*
* try {
            //1 获取code值，临时票据，类似于验证码
            //2 拿着code,appid和appSecret请求 微信固定的地址，得到两个值 accsess_token 和 openid

            System.out.println("callback");
            System.out.println("code " + code + " " + state );
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数 ：id  秘钥 和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    constantWxUtils.getAppId(),
                    constantWxUtils.getAppSecret(),
                    code
            );
            //请求这个拼接好的地址，得到返回两个值 accsess_token 和 openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串获取出来两个值 accsess_token 和 openid
            //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
            //使用json转换工具 Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");


            System.out.println("openid " + openid);

            //把扫描人信息添加数据库里面
            //判断数据表里面是否存在相同微信信息，根据openid判断
            //UcenterMember member = ucenterMemberService.getOpenIdMember(openid);
            UserInfoEntity member = null;
            if(member == null) {//memeber是空，表没有相同微信数据，进行添加

                //3 拿着得到accsess_token 和 openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userinfo字符串扫描人信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String)userInfoMap.get("nickname");//昵称
                String headimgurl = (String)userInfoMap.get("headimgurl");//头像

//                member = new UcenterMember();
//                member.setOpenid(openid);
//                member.setNickname(nickname);
//                member.setAvatar(headimgurl);
//                ucenterMemberService.save(member);
            }

            //使用jwt根据member对象生成token字符串
            //String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后：返回首页面，通过路径传递token字符串(不能直接设置到cookie中，因为cookie不能解决跨域问题)
            //return "redirect:http://localhost:3000?token=";
        }catch(Exception e) {
            throw new Exception("Fail");
        }
        *
       @GetMapping("/login")
    public void login() throws Exception {

        //微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";


        //对redirect_url进行URLEncoder编码
        String redirectUrl = constantWxUtils.getRedirectUrl();
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }
        //设置%s的值
        String url = String.format(
                baseUrl,
                constantWxUtils.getAppId(),
                redirectUrl,
                "jaycase"
        );


        //重定向到请求微信地址
        System.out.println("url " + url);
        return "redirect:" + url;
    }

    @GetMapping("/login_url")
    @ResponseBody
    public JsonData getWxCode() throws UnsupportedEncodingException {

        System.out.println("logiun");

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";

        //对redict_url进行编码

        String redictUrl = constantWxUtils.getRedirectUrl();
        String url = String.format(baseUrl,
                constantWxUtils.getAppId(),
                redictUrl
        );
        System.out.println("uri, " + url);
        return JsonData.buildSuccess(url);
    }


*
*
*
* */
