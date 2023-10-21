package com.lamp.lantern.plugins.core.login.token;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenCreateService;
import com.lamp.lantern.plugins.core.token.TokenService;
import io.lettuce.core.SetArgs;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class CreateTokenAuthHandler extends AbstractAuthHandler<TokenAndSessionConfig> {

    private TokenCreateService tokenCreateService;

    private TokenConfig tokenConfig = null;

    @Override
    public void init() {
        if (Objects.isNull(tokenConfig)) {
            tokenConfig = new TokenConfig();
            tokenConfig.setTokenCreateMode(config.getTokenCreateMode());
            tokenConfig.setTokenName(config.getTokenName());
        }

        tokenCreateService = TokenService.createTokenService(tokenConfig);
        if (Objects.isNull(LanternContext.getContext().getSessionWorkInfo())) {
            LanternContext.getContext().setSessionWorkInfo(LanternContext.getContext().new SessionWorkInfo());
        }
        LanternContext.getContext().getSessionWorkInfo().setConnection(connection);
        LanternContext.getContext().getSessionWorkInfo().setTokenHandlerName(getHandlerName());
    }

    @Override
    public void doAuthAfter(UserInfo userInfo) {
        TokenConstructData tokenConstructData = new TokenConstructData();
        String token = tokenCreateService.createToken(tokenConstructData);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("User-Agent", LanternContext.getContext().getRequest().getHeader("User-Agent"));
        jsonObject.put("UserID", userInfo.getUiId().toString() );
        String IP = LanternContext.getContext().getRequest().getHeader("X-Forwarded-For");
        IP = Objects.isNull(IP) ? LanternContext.getContext().getRequest().getRemoteAddr() : IP;
        jsonObject.put("IP", IP);
        jsonObject.put("Status", "Normal");
        SetArgs setArgs = SetArgs.Builder.ex(config.getTokenExpire());
        connection.sync().set(SystemName + "-" + getHandlerName() + "-" + token, jsonObject.toJSONString(), setArgs);
        LanternContext.getContext().setToken(token);
        HttpServletResponse response = LanternContext.getContext().getResponse();
        if (Objects.equals("cookie", super.config.getDataPosition())) {
            Cookie cookie = new Cookie(tokenConfig.getTokenName(), token);
            cookie.setDomain(config.getCookieDomain());
            cookie.setMaxAge(config.getCookieMaxAge());
            cookie.setSecure(config.isCookieSecure());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } else {
            response.addHeader(tokenConfig.getTokenName(), token);
        }
    }
}
/*
//session Redis
如果被踢出了, 应该在下一次获取后把这个session删除
//systemName-HANDLER_SYSTEM_NAME-sessionID -> JSON:{"User-Agent","IP","UserID","Status"}
//Status: Normal:正常登录
//        KickOut:被踢出


*/