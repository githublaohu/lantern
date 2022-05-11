package com.lamp.lantern.service.action.login.security;

import com.lamp.lantern.service.core.entity.enums.LoginPatternEnum;

import javax.servlet.http.HttpSession;

public interface HttpSessionService {

    void loginSuccessAndAddSession(LoginPatternEnum loginPatternEnum, Long userId, HttpSession session);

    void logoutAndDeleteSession(LoginPatternEnum loginPatternEnum, Long userId);

}
