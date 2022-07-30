package com.lamp.lantern.plugins.core.token;

public interface TokenService {

    public String createToken(TockenConstructData tockenConstructData);

    public long verifyToken(String token);
}
