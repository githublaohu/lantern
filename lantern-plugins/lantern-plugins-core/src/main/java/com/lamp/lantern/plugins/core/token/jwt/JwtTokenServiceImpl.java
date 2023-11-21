package com.lamp.lantern.plugins.core.token.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lamp.lantern.plugins.core.token.TokenConfig;
import com.lamp.lantern.plugins.core.token.TokenConstructData;
import com.lamp.lantern.plugins.core.token.TokenCreateService;

import io.jsonwebtoken.*;

public class JwtTokenServiceImpl implements TokenCreateService{

    static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 10;

    static final String SECRET = "HBHGQCXZCSGDSB";

    private TokenConfig tokenConfig;

    @Override
    public void config(TokenConfig tokenConfig) {
    	this.tokenConfig = tokenConfig;
    }


    public String createToken(String userName, long id) {
        Map<String, Object> header = new HashMap<>();

        header.put("typ","JWT");
        header.put("alg","HS256");
        
        JwtBuilder jwtBuilder = Jwts.builder().setHeader(header)
                .setId(String.valueOf(id))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setSubject(userName)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET);
        return jwtBuilder.compact();
    }

    @Override
    public String createToken(TokenConstructData tokenConstructData) {
        return "";
    }

    @Override
    public long verifyToken(String token) {
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            //TODO token 过期异常处理
            return -1;
        }
        String id = claims.getId();
        return Long.valueOf(id);
    }
}
