package com.lamp.lantern.plugins.core.login;

import com.lamp.lantern.plugins.api.service.AuthService;
import com.lamp.lantern.plugins.core.login.config.LoginConfig;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laohu
 */
public class LanternContext {

    private static final ThreadLocal<LanternContext> LOGIN_CONTEXT_LOCAL = ThreadLocal.withInitial(LanternContext::new);


    public static LanternContext getContext() {
        return LOGIN_CONTEXT_LOCAL.get();
    }

    private final Map<String, Object> values = new HashMap<String, Object>();

    @Getter
    @Setter(lombok.AccessLevel.PROTECTED)
    private AuthService authService;

    @Getter
    @Setter(lombok.AccessLevel.PROTECTED)
    private LoginConfig loginConfig;

    @Getter
    @Setter
    private HttpServletResponse response;

    @Getter
    @Setter
    private HttpServletRequest request;

    @Getter
    @Setter
    /**
     * TokenHandler里面创建的token
     */
    private String token;

    @Getter
    @Setter
    private SessionWorkInfo sessionWorkInfo;

    @Data
    public class SessionWorkInfo {
        private StatefulRedisConnection<String,String> connection;
        private String tokenHandlerName;
        private String systemName;
    }

    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) values.get(key);
    }


    protected void clear() {
        this.authService = null;
        this.values.clear();
    }

}
