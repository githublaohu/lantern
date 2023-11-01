package com.lamp.lantern.plugins.auth.qrcode;

import com.alibaba.fastjson.JSONObject;
import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.annotation.AuthTypeChannel;
import com.lamp.lantern.plugins.api.config.LoginType;
import com.lamp.lantern.plugins.api.mode.AuthResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.api.service.AbstractAuthService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@AuthTypeChannel(loginType = LoginType.QRCODE, authChannel = "Web")
@Component("qrcodeAuthService")
public class QrcodeAuthService extends AbstractAuthService {

    private StatefulRedisConnection<String, String> redisConnection;

    @Override
    public AuthResultObject auth(UserInfo userInfo) {
        //QrCode登录,不需要验证
        return null;
    }

    @Override
    public AuthResultObject getUserInfo(UserInfo userInfo) {
        throw new UnsupportedOperationException();
    }


    public AuthResultObject getUserInfo(UserInfo userInfo, StatefulRedisConnection<String, String> tokenRedisConnection, String handlerName, String systemName) {
        AuthResultObject authResultObject = AuthResultObject.create();
        //Redis结构在CreateTokenAuthHandler中
        //systemName-HANDLER_SYSTEM_NAME-sessionID -> JSON:{"User-Agent","IP","UserID","UserInfo","Status"}
        String key = systemName + "-" + handlerName + "-" + userInfo.getToken();
        String result = tokenRedisConnection.sync().get(key);
        if (Objects.isNull(result)) {
            authResultObject.setErrorMessage("手机端登录已失效");
            return authResultObject;
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!Objects.equals(jsonObject.getString("Status"), "Normal")) {
            authResultObject.setErrorMessage("手机端登录已失效");
            return authResultObject;
        }
        userInfo = JSONObject.parseObject(jsonObject.getString("UserInfo"), UserInfo.class);
        authResultObject.setUserInfo(userInfo);
        return authResultObject;
    }


    public QrcodeAuthService.QrInfo getQrcodeId(JSONObject jsonObject) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String code = UUID.randomUUID().toString().replaceAll("-", "");

        String encodeStr = encrypt(uuid);
        String codeKey = config.getQrcodeKeyPrefix() + "code:" + code;
        redisConnection.sync().hset(codeKey, "Status", "NotScanned");
        redisConnection.sync().hset(codeKey, "Web", jsonObject.toJSONString());
        redisConnection.sync().expire(codeKey, Objects.isNull(config.getQrcodeExpire()) ? 60 : config.getQrcodeExpire());

        redisConnection.sync().set(config.getQrcodeKeyPrefix() + "qrId:" + uuid, code);

        QrInfo qrInfo = new QrInfo();
        qrInfo.setCode(code);
        qrInfo.setQrId(encodeStr);
        return qrInfo;
    }

    public QrcodeStatus checkQrcodeState(String encryptId) {
        String code = redisConnection.sync().get(config.getQrcodeKeyPrefix() + "qrId:" + decrypt(encryptId));
        if (Objects.isNull(code)) {
            return null;
        }
        QrcodeStatus result = checkQrcodeStateByCode(code);
        return Objects.nonNull(result) ? result : null;
    }

    public ResultObject<String> scan(String encyptId, JSONObject mobileInfo) {
        String qrId = decrypt(encyptId);
        String code = redisConnection.sync().get(config.getQrcodeKeyPrefix() + "qrId:" + qrId);
        if (Objects.isNull(code)) {
            return ResultObject.getResultObjectMessgae(4000, "二维码已失效");
        }

        String codeKey = config.getQrcodeKeyPrefix() + "code:" + code;
        redisConnection.sync().hset(codeKey, "Status", "Scanned");
        //set mobile info
        redisConnection.sync().hset(codeKey, "Mobile", mobileInfo.toJSONString());


        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisConnection.sync().hset(codeKey, "token", token);
        return ResultObject.getResultObjectMessgae(200, token);
    }

    //    private static String encrypt(String uuid){
//        byte[] bytes = null;
//        try {
//            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//            messageDigest.update(uuid.getBytes("UTF-8"));
//            bytes = messageDigest.digest();
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        StringBuffer stringBuffer = new StringBuffer();
//        String temp = null;
//        for (int i=0;i<bytes.length;i++){
//            temp = Integer.toHexString(bytes[i] & 0xFF);
//            if (temp.length()==1){
//                //1得到一位的进行补0操作
//                stringBuffer.append("0");
//            }
//            stringBuffer.append(temp);
//        }
//        return stringBuffer.toString();
//    }
    //TODO 加密解密
    private static String encrypt(String uuid) {
        return uuid;
    }

    private static String decrypt(String encryptId) {
        return encryptId;
    }

    public void doInitialization() {
        try {
            redisConnection = RedisClient.create(config.getRedisUrl()).connect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public ResultObject<String> deny(String encryptId, String token) {
        String qrId = decrypt(encryptId);
        String code = redisConnection.sync().get(config.getQrcodeKeyPrefix() + "qrId:" + qrId);
        if (Objects.isNull(code)) {
            return ResultObject.getResultObjectMessgae(4000, "二维码已失效");
        }
        String codeKey = config.getQrcodeKeyPrefix() + "code:" + code;
        String tokenInRedis = redisConnection.sync().hget(codeKey, "token");
        if (!Objects.equals(token, tokenInRedis)) {
            return ResultObject.getResultObjectMessgae(4004, "无效token");
        }
        redisConnection.sync().hset(codeKey, "Status", "Deny");
        return ResultObject.getResultObjectMessgae(200, "操作成功");
    }

    public ResultObject<String> mobileAuth(String encryptId, String token) {
        String qrId = decrypt(encryptId);
        String code = redisConnection.sync().get(config.getQrcodeKeyPrefix() + "qrId:" + qrId);
        if (Objects.isNull(code)) {
            return ResultObject.getResultObjectMessgae(4000, "二维码已失效");
        }
        String codeKey = config.getQrcodeKeyPrefix() + "code:" + code;
        String tokenInRedis = redisConnection.sync().hget(codeKey, "token");
        if (!Objects.equals(token, tokenInRedis)) {
            return ResultObject.getResultObjectMessgae(4004, "无效token");
        }
        redisConnection.sync().hset(codeKey, "Status", "Confirmed");
        return ResultObject.getResultObjectMessgae(200, "操作成功");
    }

    public QrcodeStatus checkQrcodeStateByCode(String code) {
        String result = redisConnection.sync().hget(config.getQrcodeKeyPrefix() + "code:" + code, "Status");
        return Objects.nonNull(result) ? QrcodeStatus.valueOf(result) : null;
    }

    public String getUserToken(String code) {
        String mobile = redisConnection.sync().hget(config.getQrcodeKeyPrefix() + "code:" + code, "Mobile");
        if (Objects.isNull(mobile)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(mobile);
        return jsonObject.getString("token");
    }

    public enum QrcodeStatus {
        NotScanned, Scanned, Confirmed, Timeout, Deny
    }

    public enum MobileOperate {
        Scan, Auth, Deny
    }

    @Data
    public class QrInfo {
        String code;
        String QrId;
    }

}

/**
 * Redis结构：
 * Hash
 * prefix:qrId:qrcodeId -> code;
 * prefix:code:code -> {
 * "Status" -> "NotScanned" | "Scanned" | "Authed" | "Timeout" | "Deny"
 * "Web" -> {
 * "IP"
 * "User-Agent"
 * }
 * "Mobile"-> {
 * "IP"
 * "User-Agent"
 * "token"(手机端使用的登录token)
 * * }
 * "token" -> token(手机端使用的临时token)
 * }
 */