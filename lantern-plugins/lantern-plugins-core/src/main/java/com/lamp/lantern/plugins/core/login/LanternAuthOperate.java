package com.lamp.lantern.plugins.core.login;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

/** Existing account/password login, shared by embedded and standalone applications. */
@AuthTypeChannel(loginType = LoginType.FIRST, authChannel = "Lantern")
public class LanternAuthOperate extends AbstractAuthService {

    private final Function<UserInfo, UserInfo> userLookup;

    public LanternAuthOperate(Function<UserInfo, UserInfo> userLookup) {
        this.userLookup = Objects.requireNonNull(userLookup, "Lantern login user lookup is required");
    }

    @Override
    public AuthResultObject auth(UserInfo input) {
        AuthResultObject result = new AuthResultObject();
        if (input == null || !StringUtils.hasText(input.getUiPassword())) {
            result.setErrorMessage("用户或者密码不正确");
            return result;
        }
        UserInfo user = userLookup.apply(input);
        if (user == null || user.getUiId() == null || !StringUtils.hasText(user.getUiSaltPassword())) {
            result.setErrorMessage("用户或者密码不正确");
            return result;
        }
        String actual = DigestUtils.md5Hex(user.getUiSalt() + input.getUiPassword());
        if (!MessageDigest.isEqual(user.getUiSaltPassword().getBytes(StandardCharsets.UTF_8),
                actual.getBytes(StandardCharsets.UTF_8))) {
            result.setErrorMessage("用户或者密码不正确");
            return result;
        }
        user.setUiPassword(null);
        user.setUiSalt(null);
        user.setUiSaltPassword(null);
        user.setUiToken(null);
        result.setUserInfo(user);
        return result;
    }

    @Override
    public AuthResultObject getUserInfo(UserInfo userInfo) {
        AuthResultObject result = new AuthResultObject();
        result.setUserInfo(userInfo);
        return result;
    }
}
