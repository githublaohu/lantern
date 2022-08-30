package com.lamp.lantern.plugins.core.login.times;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstrackAuthHandler;
import com.lamp.lantern.plugins.core.login.LoginContext;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginTimesAuthHandler extends AbstrackAuthHandler<LoginTimesConfig> {

	private static final String VALUE_KEY = "loginTimesAuthHandler";
	
	private ResultObject<String> errer;

	public void init() {
		errer = ResultObject.getResultObjectMessgae(3000, "登录失败次数到限制，请明天登录");
	}

	public ResultObject<String> authBefore(UserInfo userInfo) {
		LoginTimesInfo loginTimesInfo = new LoginTimesInfo();
		LoginContext.getContext().setValue(VALUE_KEY, loginTimesInfo);
		if (config.getAddressTimeLong() > 0) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			request.getRemoteAddr();
			String key = "";
			loginTimesInfo.setAddressLimesKey(key);
			Integer integer = Integer.valueOf(connection.sync().get(key));
			loginTimesInfo.setAddressLimes(Objects.isNull(integer) ? -1 : integer);
		}
		if (config.getTimes() > 0) {
			String key = "";
			loginTimesInfo.setTimesKey(key);
			Integer integer = Integer.valueOf(connection.sync().get(key));
			loginTimesInfo.setTimes(Objects.isNull(integer) ? -1 : integer);
		}
		if (loginTimesInfo.getTimes() >= config.getTimes()
				|| loginTimesInfo.getAddressLimes() >= config.getAddressLimes()) {
			log.warn("");
			return  errer;
		}
		return null;

	}

	@Override
	public void doAuthAfter(UserInfo userInfo) {
		LoginTimesInfo loginTimesInfo = LoginContext.getContext().getValue(VALUE_KEY);
		if (Objects.nonNull(loginTimesInfo)) {
			return;
		}
		if (loginTimesInfo.getTimes() != -1) {
			connection.async().del(loginTimesInfo.getTimesKey());
		}
		if (loginTimesInfo.getAddressLimes() != -1) {
			connection.async().del(loginTimesInfo.getAddressLimesKey());
		}
	}

	public void doErrer(UserInfo userInfo) {
		LoginTimesInfo loginTimesInfo = LoginContext.getContext().getValue(VALUE_KEY);
		if (config.getTimes() > 0) {
			connection.sync().expire(loginTimesInfo.getTimesKey(), 1);
			if (loginTimesInfo.getTimes() <= config.getTimes()) {
				connection.sync().incr(loginTimesInfo.getTimesKey());
			}
		}
		if (config.getAddressLimes() > 0) {
			if (loginTimesInfo.getAddressLimes() <= config.getAddressLimes()) {
				connection.sync().incr(loginTimesInfo.getTimesKey());
			}
			if (!Objects.equals(TimeUnit.DAYS, config.getAddressTimeUnit())) {
				connection.sync().expire(loginTimesInfo.getTimesKey(), 1);
			} else {
				if (loginTimesInfo.getAddressLimes() == -1) {
					connection.sync().expire(loginTimesInfo.getTimesKey(), 1);
				}
			}
		}
	}

	@Data
	static class LoginTimesInfo {

		private Integer times;

		private Integer addressLimes;

		private String timesKey;

		private String addressLimesKey;

	}
}
