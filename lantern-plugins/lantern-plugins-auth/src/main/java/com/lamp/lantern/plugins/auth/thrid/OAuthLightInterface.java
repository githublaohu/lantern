package com.lamp.lantern.plugins.auth.thrid;

import com.lamp.light.api.http.annotation.method.POST;

//因为依赖问题，暂时使用PostHelper发送请求
public interface OAuthLightInterface {
    @POST("/login/oauth/access_token?code={code}&client_id={client_id}&client_secret={client_secret}")
    ReturnObject getGithubAccessToken(String code, String client_id, String client_secret);
}
