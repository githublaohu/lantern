package com.lamp.lantern.plugins.core.login;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatClient;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatConfig;
import java.util.UUID;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

/** OAuth state validation followed by the same login handlers used for account/password login. */
public class EnterpriseWechatLogin {

    private final EnterpriseWechatConfig config;
    private final EnterpriseWechatClient client;
    private final EnterpriseWechatStateStore states;
    private final HandlerExecute login;

    public EnterpriseWechatLogin(EnterpriseWechatConfig config, EnterpriseWechatClient client,
            EnterpriseWechatStateStore states, HandlerExecute login) {
        this.config = config;
        this.client = client;
        this.states = states;
        this.login = login;
    }

    public Authorization start() {
        String state = random();
        String browser = random();
        states.save(DigestUtils.sha256Hex(state), DigestUtils.sha256Hex(browser), config.getStateTtlSeconds());
        return new Authorization(client.authorizeUrl(state), browser);
    }

    public ResultObject<?> callback(String code, String state, String browser) {
        if (!validNonce(state) || !validNonce(browser)
                || !states.consume(DigestUtils.sha256Hex(state), DigestUtils.sha256Hex(browser))) {
            return ResultObject.getResultObjectMessgae(401, "企业微信登录请求无效或已过期");
        }
        UserInfo input = new UserInfo();
        input.setUiToken(code);
        return login.execute(input, LoginType.PLATFORM, EnterpriseWechatConfig.CHANNEL);
    }

    private static String random() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static boolean validNonce(String value) {
        return value != null && value.matches("[a-f0-9]{32}");
    }

    @Getter
    public static class Authorization {
        private final String url;
        private final String browser;

        Authorization(String url, String browser) {
            this.url = url;
            this.browser = browser;
        }
    }
}
