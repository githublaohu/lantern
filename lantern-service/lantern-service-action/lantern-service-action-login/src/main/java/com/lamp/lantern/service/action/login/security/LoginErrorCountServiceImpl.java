package com.lamp.lantern.service.action.login.security;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.apache.dubbo.config.annotation.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoginErrorCountServiceImpl implements LoginErrorCountService{

    // 多线程 安全还没做

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private LocalDateTime time = LocalDateTime.now();

    private static class LoginErrorRecord{

        // key 是 user_name value 是登录错误次数
        private Map<String, AtomicInteger> records = new ConcurrentHashMap<>();
    }

    // 第一个key 是 时间按照天来保存  第二个key是 第三方平台Id
    private Map<String, Map<Integer, LoginErrorRecord>> loginErrorRecordMap = new ConcurrentHashMap<>();

    @Override
    public int countUserLoginError(UserInfoEntity userInfoEntity) {

        String todayDateFormat = time.format(formatter);
        int triId = userInfoEntity.getTriId();
        String userName = userInfoEntity.getUiName();

        if (!loginErrorRecordMap.keySet().contains(todayDateFormat)){
            return 0;
        }
        Map<Integer, LoginErrorRecord> dateFormatLoginRecord = loginErrorRecordMap.get(todayDateFormat);

        if(!dateFormatLoginRecord.keySet().contains(triId)){
            return 0;
        }
        LoginErrorRecord loginErrorRecord = dateFormatLoginRecord.get(triId);

        if(!loginErrorRecord.records.keySet().contains(userName)){
            return 0;
        }
        return loginErrorRecord.records.get(userName).get();
    }

    @Override
    public void refershUserLoginError(UserInfoEntity userInfoEntity){
        String todayDateFormat = time.format(formatter);
        int triId = userInfoEntity.getTriId();
        String userName = userInfoEntity.getUiName();

        Map<Integer, LoginErrorRecord> dateFormatLoginRecord = loginErrorRecordMap.get(todayDateFormat);
        LoginErrorRecord loginErrorRecord = dateFormatLoginRecord.get(triId);

        loginErrorRecord.records.put(userName, new AtomicInteger(0));

    }

    @Override
    public void insertUserLoginError(UserInfoEntity userInfoEntity){
        System.out.println(loginErrorRecordMap);
        String todayDateFormat = time.format(formatter);
        int triId = userInfoEntity.getTriId();
        String userName = userInfoEntity.getUiName();

        if(!loginErrorRecordMap.keySet().contains(todayDateFormat)){
            loginErrorRecordMap.put(todayDateFormat, new ConcurrentHashMap<Integer, LoginErrorRecord>());
        }

        Map<Integer, LoginErrorRecord> dateFormatLoginRecord = loginErrorRecordMap.get(todayDateFormat);

        if(!dateFormatLoginRecord.keySet().contains(triId)){
            dateFormatLoginRecord.put(triId, new LoginErrorRecord());
        }

        LoginErrorRecord loginErrorRecord = dateFormatLoginRecord.get(triId);

        if(!loginErrorRecord.records.keySet().contains(userName)){
            loginErrorRecord.records.put(userName, new AtomicInteger(1));
        }else{
            loginErrorRecord.records.get(userName).incrementAndGet();
        }
    }

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String todayDateFormat = time.format(formatter);
        System.out.println(todayDateFormat);
    }

}
