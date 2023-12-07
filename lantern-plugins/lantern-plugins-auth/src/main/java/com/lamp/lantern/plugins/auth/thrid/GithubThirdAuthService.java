package com.lamp.lantern.plugins.auth.thrid;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.PlatformUserInfo;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractThirdAuthService;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.HashMap;

@Slf4j
@AuthTypeChannel(loginType = LoginType.THIRD, authChannel = "Github")
public class GithubThirdAuthService extends AbstractThirdAuthService {
    @Override
    public AuthResultObject auth(UserInfo userInfo) {
//        Light.Builder builder = Light.Builder();
//        Light light = builder.host("github.com").port(443).tls(true).build();
//        OAuthLightInterface oAuthLightInterface = null;
//        try {
//            oAuthLightInterface = light.create(OAuthLightInterface.class);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        ReturnObject res = oAuthLightInterface.getGithubAccessToken(userInfo.getToken(), config.getAppId(), config.getSecretAccessKey());
//        return null;
        String url = "https://github.com/login/oauth/access_token?code={0}&client_id={1}&client_secret={2}";
        url = MessageFormat.format(url, userInfo.getUiToken(), config.getAppId(), config.getSecretAccessKey());
        String token = PostHelper.Post(url,"POST",new HashMap<>());
        if (token.contains("error")) {
            log.error("github auth error: {}", token);
            return null;
        }
        token = token.substring(token.indexOf("=")+1,token.indexOf("&"));
        AuthResultObject authResultObject = AuthResultObject.create();
        userInfo.setUiToken(token);
        authResultObject.setUserInfo(userInfo);
        return authResultObject;
    }

    @Override
    public AuthResultObject getUserInfo(UserInfo userInfo) {
        AuthResultObject authResultObject = AuthResultObject.create();
        String token = "Bearer "+ userInfo.getUiToken();
        //get user info
        String url = "https://api.github.com/user";
        String res = PostHelper.Post(url,"GET",new HashMap<String,String>(){{
            put("Authorization",token);
        }});
        JSONObject jsonObject = JSONObject.parseObject(res);
        userInfo.setUiName(jsonObject.getString("login"));
        PlatformUserInfo platformUserInfo =  new PlatformUserInfo();
        platformUserInfo.setPuiOpenId(jsonObject.getString("id"));
        platformUserInfo.setPuiType(LoginType.THIRD.getType());
        platformUserInfo.setPuiAuthChannel("Github");
        platformUserInfo.setPuiUnionId("0");

        authResultObject.setPlatformUserInfo(platformUserInfo);
        authResultObject.setUserInfo(userInfo);
        return authResultObject;
    }

    @Override
    public RedirectAddress getRedirectAddress() {
        String url = "https://github.com/login/oauth/authorize?client_id={0}&redirect_uri={1}&scope=user";
        url = MessageFormat.format(url, config.getAppId(), (config.getRedirectUri()));
        return RedirectAddress.create(url);
    }
}

/**
 * {
 *     "login": "Lambert-Rao",
 *     "id": ,
 *     "node_id": "",
 *     "avatar_url": "https://avatars.githubusercontent.com/u/101875596?v=4",
 *     "gravatar_id": "",
 *     "url": "https://api.github.com/users/Lambert-Rao",
 *     "html_url": "https://github.com/Lambert-Rao",
 *     "followers_url": "https://api.github.com/users/Lambert-Rao/followers",
 *     "following_url": "https://api.github.com/users/Lambert-Rao/following{/other_user}",
 *     "gists_url": "https://api.github.com/users/Lambert-Rao/gists{/gist_id}",
 *     "starred_url": "https://api.github.com/users/Lambert-Rao/starred{/owner}{/repo}",
 *     "subscriptions_url": "https://api.github.com/users/Lambert-Rao/subscriptions",
 *     "organizations_url": "https://api.github.com/users/Lambert-Rao/orgs",
 *     "repos_url": "https://api.github.com/users/Lambert-Rao/repos",
 *     "events_url": "https://api.github.com/users/Lambert-Rao/events{/privacy}",
 *     "received_events_url": "https://api.github.com/users/Lambert-Rao/received_events",
 *     "type": "User",
 *     "site_admin": false,
 *     "name": "Lambert Rao",
 *     "company": null,
 *     "blog": "https://lambertrao.top",
 *     "location": "Changsha,China ",
 *     "email": null,
 *     "hireable": true,
 *     "bio": "An amateur computer science enthusiast",
 *     "twitter_username": null,
 *     "public_repos": 12,
 *     "public_gists": 0,
 *     "followers": 8,
 *     "following": 14,
 *     "created_at": "2022-03-18T12:41:44Z",
 *     "updated_at": "2023-10-19T14:54:53Z"
 * }
 */