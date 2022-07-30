package com.lamp.lantern.service.action.login.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamp.lantern.service.action.login.function.ConstantWxUtils;
import com.lamp.lantern.service.action.login.incident.IncidentService;
import com.lamp.lantern.service.action.login.security.Decript;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/ucenter/wx")
@RestController("/api/ucenter/wx")
@Api(tags = {"三方登录"})
public class TripartiteLoginController {

    @Autowired
    private ConstantWxUtils constantWxUtils;

    @Autowired
    private IncidentService incidentService;

    @RequestMapping("/login")
    public String getWxCode(){

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect"+
                "?appid=%s"+
                "&redirect_uri=%s"+
                "&response_type=%s"+
                "&scope=snsapi_login"+
                "&state=%s"+
                "#wechat_redirect";

        //对redict_url进行编码
        String redictUrl = constantWxUtils.getRedirectUrl();
        try {
            URLEncoder.encode(redictUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(baseUrl,
                constantWxUtils.getAppId(),
                redictUrl,
                "code",
                "coderkun");

        //请求微信地址
        return "redirect:"+url;
    }


    @RequestMapping("/callback")
    public String callback(String signature, String timestamp, String nonce, String echostr){

        ArrayList<String> array = new ArrayList<String>();
        array.add(signature);
        array.add(timestamp);
        array.add(nonce);

        //排序
        String sortString = sort("jaycase", timestamp, nonce);
        //加密
        String mytoken = Decript.SHA1(sortString);
        //校验签名
        if (mytoken != null && mytoken != "" && mytoken.equals(signature)) {
            System.out.println("签名校验通过。");
            return echostr; //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
        } else {
            System.out.println("签名校验失败。");
            return null;
        }

    }

    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = { token, timestamp, nonce };
        Arrays.sort(strArray);

        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }

        return sbuilder.toString();
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
*
*
* */
