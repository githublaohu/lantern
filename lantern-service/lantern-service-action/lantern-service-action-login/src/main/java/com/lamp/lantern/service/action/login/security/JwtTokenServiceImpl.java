package com.lamp.lantern.service.action.login.security;

import io.jsonwebtoken.*;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenServiceImpl implements JwtTokenService{

    @Override
    public String createToken(String user_name, long id) {
        Map<String, Object> header = new HashMap<>();

        header.put("typ","JWT");
        header.put("alg","HS256");

        JwtBuilder jwtBuilder = Jwts.builder().setHeader(header)
                .setId(String.valueOf(id))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setSubject(user_name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET);
        return jwtBuilder.compact();
    }

    @Override
    public long verifyToken(String token) {
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            // token 过期异常处理
            return -1;
        }
        String id = claims.getId();
        return Long.valueOf(id);
    }
}
