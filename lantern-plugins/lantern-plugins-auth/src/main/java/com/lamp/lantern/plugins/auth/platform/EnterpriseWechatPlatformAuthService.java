/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.auth.platform;

import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import java.util.function.Function;

@AuthTypeChannel(loginType = LoginType.PLATFORM, authChannel = EnterpriseWechatConfig.CHANNEL)
public class EnterpriseWechatPlatformAuthService extends AbstractAuthService{

    private final EnterpriseWechatClient client;
    private final Function<String, UserInfo> memberLookup;

    public EnterpriseWechatPlatformAuthService(EnterpriseWechatClient client, Function<String, UserInfo> memberLookup) {
        this.client = java.util.Objects.requireNonNull(client, "Enterprise WeChat client is required");
        this.memberLookup = java.util.Objects.requireNonNull(memberLookup, "Enterprise WeChat member lookup is required");
    }

    @Override
    public AuthResultObject auth(UserInfo input) {
        AuthResultObject result = new AuthResultObject();
        // The callback constructs this input; uiToken temporarily carries the single-use OAuth code.
        String member = input == null ? null : client.memberId(input.getUiToken());
        UserInfo user = member == null ? null : memberLookup.apply(member);
        if (user == null || user.getUiId() == null) {
            result.setErrorMessage("企业微信成员未绑定有效用户");
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
    public AuthResultObject getUserInfo(UserInfo input) {
        return auth(input);
    }

}
