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
package com.lamp.lantern.plugins.core.login.times;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstractAuthHandler;
import com.lamp.lantern.plugins.core.login.LanternContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginTimesAuthHandler extends AbstractAuthHandler<LoginTimesConfig> {

    private static final String VALUE_KEY = "loginTimesAuthHandler";

    private String addressTimeKey;

    private String userIdentificationKey;

    private ResultObject<String> error;

    @Override
    public void init() {
        error = ResultObject.getResultObjectMessgae(3000, "登录失败次数到限制，请明天登录");
        this.addressTimeKey = this.SystemName + "-" + "address-time";
        this.userIdentificationKey = this.SystemName + "-" + "user-time";
    }


    @Override
    public ResultObject<String> authBefore(UserInfo userInfo) {
        LoginTimesInfo loginTimesInfo = new LoginTimesInfo();
        LanternContext.getContext().setValue(VALUE_KEY, loginTimesInfo);
        if (config.getAddressTimes() > 0) {
            HttpServletRequest request = LanternContext.getContext().getRequest();
            String IP = request.getHeader("x-forwarded-for");
            String key = this.addressTimeKey + (Objects.isNull(IP) ? request.getRemoteAddr() : IP);
            loginTimesInfo.setAddressTimesKey(key);
            String value = connection.sync().get(key);
            Integer integer = Objects.isNull(value) ? null : Integer.valueOf(value);
            loginTimesInfo.setAddressTimes(Objects.isNull(integer) ? -1 : integer);

            if (loginTimesInfo.getAddressTimes() >= config.getAddressTimes()) {
                log.warn("");
                //登录失败
                return error;
            }
        }
        if (config.getTimes() > 0) {
            String key = this.userIdentificationKey + (Objects.nonNull(userInfo.getUiName()) ? userInfo.getUiName()
                    : Objects.nonNull(userInfo.getUiEmail()) ? userInfo.getUiEmail() : userInfo.getUiPhone());
            loginTimesInfo.setTimesKey(key);
            String value = connection.sync().get(key);
            Integer integer = Objects.isNull(value) ? null : Integer.valueOf(value);
            loginTimesInfo.setTimes(Objects.isNull(integer) ? -1 : integer);

            if (loginTimesInfo.getTimes() >= config.getTimes()) {
                log.warn("");
                //登录失败
                return error;
            }
        }
        return null;

    }

    @Override
    public void doAuthAfter(UserInfo userInfo) {
        LoginTimesInfo loginTimesInfo = LanternContext.getContext().getValue(VALUE_KEY);
        if (Objects.isNull(loginTimesInfo)) {
            return;
        }
        if (Objects.nonNull(loginTimesInfo.getTimes())&& loginTimesInfo.getTimes() != -1) {
            connection.async().del(loginTimesInfo.getTimesKey());
        }
        if (Objects.nonNull(loginTimesInfo.getAddressTimes())&&loginTimesInfo.getAddressTimes() != -1) {
            connection.async().del(loginTimesInfo.getAddressTimesKey());
        }
    }

    @Override
    public void doError(UserInfo userInfo) {
        LoginTimesInfo loginTimesInfo = LanternContext.getContext().getValue(VALUE_KEY);
        // 当times大于0，表示启动用户限制
        if (config.getTimes() > 0) {
            if (loginTimesInfo.getTimes() < config.getTimes()) {
                connection.async().incr(loginTimesInfo.getTimesKey());
                if (Objects.equals(TimeUnit.DAYS, config.getAddressTimeUnit()) && loginTimesInfo.getTimes() == -1) {
                    connection.async().expireat(loginTimesInfo.getTimesKey(), Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                } else {
                    connection.async().expire(loginTimesInfo.getTimesKey(), config.getTimeLong());
                }
            }
        }
        // 当address-times大于0，表示启动IP限制
        if (config.getAddressTimes() > 0) {
            // 当前限制数 小于 规定限制数量就小于自增，并且规定时间
            if (loginTimesInfo.getAddressTimes() < config.getAddressTimes()) {
                // 自增
                connection.async().incr(loginTimesInfo.getAddressTimesKey());
                //单位是天，且是第一次
                if (Objects.equals(TimeUnit.DAYS, config.getAddressTimeUnit()) && loginTimesInfo.getAddressTimes() == -1) {
                    connection.async().expireat(loginTimesInfo.getAddressTimesKey(), Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                } else {
                    connection.async().expire(loginTimesInfo.getAddressTimesKey(), config.getAddressTimeLong());
                }
            }
        }
    }

    @Data
    static class LoginTimesInfo {

        private Integer times;

        private Integer addressTimes;

        private String timesKey;

        private String addressTimesKey;

    }
}
