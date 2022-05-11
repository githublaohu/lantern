package com.lamp.lantern.service.action.login.security;

import com.lamp.lantern.service.core.entity.enums.LoginPatternEnum;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.apache.dubbo.config.annotation.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HttpSessionServiceImpl implements HttpSessionService{

    private Map<LoginPatternEnum, Map<Long, HttpSession>> loginSessionRecordMap = new ConcurrentHashMap<>();

    @Override
    public void loginSuccessAndAddSession(LoginPatternEnum loginPatternEnum, Long userId, HttpSession session) {
        if(!loginSessionRecordMap.containsKey(loginPatternEnum)){
            loginSessionRecordMap.put(loginPatternEnum, new ConcurrentHashMap<Long, HttpSession>());
        }

        Map<Long, HttpSession> userIdHttpSessionMap = loginSessionRecordMap.get(loginPatternEnum);

        userIdHttpSessionMap.put(userId, session);
    }

    @Override
    public void logoutAndDeleteSession(LoginPatternEnum loginPatternEnum, Long userId) {

        if(!loginSessionRecordMap.containsKey(loginPatternEnum) || !loginSessionRecordMap.get(loginPatternEnum).containsKey(userId)){
            throw new ValueException("cannot find this httpsession");
        }

        HttpSession session = loginSessionRecordMap.get(loginPatternEnum).get(userId);
        loginSessionRecordMap.get(loginPatternEnum).remove(userId);
        session.invalidate();

    }
}
