package com.lamp.lantern.service.action.login.security;

public interface JwtTokenService {

    static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 10;

    static final String SECRET = "HBHGQCXZCSGDSB";

    public String createToken(String user_name, long id);

    public long verifyToken(String token);

}
