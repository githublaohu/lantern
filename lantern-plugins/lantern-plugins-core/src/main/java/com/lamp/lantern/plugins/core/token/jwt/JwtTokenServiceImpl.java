package com.lamp.lantern.plugins.core.token.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenService;

import io.jsonwebtoken.*;

public class JwtTokenServiceImpl implements TokenService{

    static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 10;

    static final String SECRET = "HBHGQCXZCSGDSB";
	

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
    public String createToken(TokenConstructData tockenConstructData) {
        return "";
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
