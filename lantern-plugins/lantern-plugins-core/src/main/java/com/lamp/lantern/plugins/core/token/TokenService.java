package com.lamp.lantern.plugins.core.token;

public interface TokenService {

    public String createToken(TokenConstructData tockenConstructData);

    public long verifyToken(String token);
}
