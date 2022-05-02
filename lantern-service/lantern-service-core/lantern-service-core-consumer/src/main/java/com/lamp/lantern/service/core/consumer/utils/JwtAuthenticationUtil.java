package com.lamp.lantern.service.core.consumer.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtAuthenticationUtil {

    public static final String SECRET = "HGKXYZTO9342";

    public static final int calendarField = Calendar.DATE;

    public static final int calendarInterval = 10;

    public static String createToken(String user_name) {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP").withClaim("user_name", user_name)
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature

        return token;
    }

    public static Map<String, Claim> verifyToken(String token) throws Exception {

        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new Exception("Token 失效");
        }
        return jwt.getClaims();
    }

    public static String getAppUserName(String token) throws Exception {
        Map<String, Claim> claims = verifyToken(token);
        Claim user_name_claim = claims.get("user_name");
        if (null == user_name_claim || StringUtils.isEmpty(user_name_claim.asString())) {
            throw new Exception("token失效");
        }
        return user_name_claim.asString();
    }


}
