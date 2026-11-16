package com.lamp.lantern.plugins.auth.platform;

import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/** Bound from lantern.login-config.enterprise-wechat; disabled unless explicitly enabled. */
@Getter
@Setter
public class EnterpriseWechatConfig {

    public static final String CHANNEL = "EnterpriseWechat";
    public static final String AUTHORIZE_PATH = "/account/enterprise-wechat/authorize";
    public static final String CALLBACK_PATH = "/account/enterprise-wechat/callback";

    private boolean enabled;
    private String corpId;
    private String agentId;
    private String secret;
    private String redirectUri;
    private int stateTtlSeconds = 300;
    private int connectTimeoutMillis = 3000;
    private int readTimeoutMillis = 5000;

    public void validate() {
        Assert.hasText(corpId, "Enterprise WeChat corpId is required");
        Assert.isTrue(corpId.length() <= 31, "Enterprise WeChat corpId exceeds binding capacity");
        Assert.isTrue(agentId != null && agentId.matches("[0-9]{1,31}"),
                "Enterprise WeChat agentId must be numeric");
        Assert.hasText(secret, "Enterprise WeChat secret is required");
        Assert.hasText(redirectUri, "Enterprise WeChat redirectUri is required");
        URI callback = URI.create(redirectUri);
        boolean local = "http".equals(callback.getScheme()) && ("localhost".equals(callback.getHost())
                || "127.0.0.1".equals(callback.getHost()) || "[::1]".equals(callback.getHost()));
        Assert.isTrue(("https".equals(callback.getScheme()) || local) && callback.getHost() != null
                        && callback.getUserInfo() == null && callback.getQuery() == null && callback.getFragment() == null
                        && callback.getPath() != null && callback.getPath().endsWith(CALLBACK_PATH),
                "Enterprise WeChat redirectUri must be an HTTPS Lantern callback (HTTP only on loopback)");
        Assert.isTrue(stateTtlSeconds > 0 && stateTtlSeconds <= 600,
                "Enterprise WeChat stateTtlSeconds must be between 1 and 600");
        Assert.isTrue(connectTimeoutMillis > 0 && connectTimeoutMillis <= 30000
                        && readTimeoutMillis > 0 && readTimeoutMillis <= 30000,
                "Enterprise WeChat HTTP timeouts must be between 1 and 30000 milliseconds");
    }
}
