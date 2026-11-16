package com.lamp.lantern.plugins.auth.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.util.StringUtils;

/** Official CorpApp Web login APIs. The application access token is cached only in this JVM. */
public class EnterpriseWechatClient {

    private static final String API = "https://qyapi.weixin.qq.com/cgi-bin/";
    private static final int MAX_RESPONSE_BYTES = 65536;
    private final EnterpriseWechatConfig config;
    private String accessToken;
    private long expiresAt;

    public EnterpriseWechatClient(EnterpriseWechatConfig config) {
        this.config = config;
    }

    public String authorizeUrl(String state) {
        return "https://login.work.weixin.qq.com/wwlogin/sso/login?login_type=CorpApp&appid="
                + encode(config.getCorpId()) + "&agentid=" + encode(config.getAgentId())
                + "&redirect_uri=" + encode(config.getRedirectUri()) + "&state=" + encode(state);
    }

    /** Returns only a server-verified enterprise member ID; external visitors cannot log in. */
    public String memberId(String code) {
        if (!StringUtils.hasText(code) || code.getBytes(StandardCharsets.UTF_8).length > 512) {
            return null;
        }
        String token = token(null);
        JSONObject identity = identity(token, code);
        int error = errorCode(identity);
        if (error == 40014 || error == 42001 || error == 40001) {
            identity = identity(token(token), code);
            error = errorCode(identity);
        }
        if (error == 40029 || error == 40163) {
            return null;
        }
        if (error != 0) {
            throw new IllegalStateException("Enterprise WeChat identity service unavailable");
        }
        String member = identity.getString("userid");
        return StringUtils.hasText(member) && member.length() <= 255 ? member : null;
    }

    private JSONObject identity(String token, String code) {
        return getJson(API + "auth/getuserinfo?access_token=" + encode(token) + "&code=" + encode(code));
    }

    private synchronized String token(String invalidToken) {
        if (accessToken != null && System.currentTimeMillis() < expiresAt
                && !accessToken.equals(invalidToken)) {
            return accessToken;
        }
        JSONObject result = getJson(API + "gettoken?corpid=" + encode(config.getCorpId())
                + "&corpsecret=" + encode(config.getSecret()));
        String value = result.getString("access_token");
        int ttl = result.getIntValue("expires_in");
        if (errorCode(result) != 0 || !StringUtils.hasText(value) || ttl <= 0 || ttl > 86400) {
            throw new IllegalStateException("Enterprise WeChat application credentials unavailable");
        }
        accessToken = value;
        expiresAt = System.currentTimeMillis() + Math.max(1, ttl - 60) * 1000L;
        return value;
    }

    private int errorCode(JSONObject result) {
        if (result == null || !(result.get("errcode") instanceof Number)) {
            throw new IllegalStateException("Invalid Enterprise WeChat response");
        }
        return result.getIntValue("errcode");
    }

    /** Kept overridable for deterministic protocol tests; endpoints are never configuration or request input. */
    protected JSONObject getJson(String address) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(address).openConnection();
            connection.setConnectTimeout(config.getConnectTimeoutMillis());
            connection.setReadTimeout(config.getReadTimeoutMillis());
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Accept", "application/json");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected response status");
            }
            try (InputStream input = connection.getInputStream();
                    ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int size;
                while ((size = input.read(buffer)) != -1) {
                    if (output.size() + size > MAX_RESPONSE_BYTES) {
                        throw new IOException("Response too large");
                    }
                    output.write(buffer, 0, size);
                }
                return JSON.parseObject(new String(output.toByteArray(), StandardCharsets.UTF_8),
                        Feature.DisableSpecialKeyDetect);
            }
        } catch (IOException | RuntimeException exception) {
            // URLs contain the application secret or access token: never propagate them or provider bodies.
            throw new IllegalStateException("Enterprise WeChat request failed");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException exception) {
            throw new IllegalStateException("UTF-8 unavailable");
        }
    }
}
