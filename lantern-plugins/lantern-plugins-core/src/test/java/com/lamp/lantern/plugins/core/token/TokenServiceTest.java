package com.lamp.lantern.plugins.core.token;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenServiceTest {

    private TokenConfig uuidConfig = new TokenConfig();
    private TokenConfig randomConfig = new TokenConfig();
    private TokenConfig encryptionConfig = new TokenConfig();
    private TokenConfig jwtConfig = new TokenConfig();

    @Before
    public void setConfig(){
        uuidConfig.setTokenCreateMode(TokenCreateMode.UUID);
        randomConfig.setTokenCreateMode(TokenCreateMode.RANDOMSTRING);
        encryptionConfig.setTokenCreateMode(TokenCreateMode.ENCRYPTION);
        encryptionConfig.setAlgorithm("AES");
        encryptionConfig.setKey("1234567890123456");
        encryptionConfig.setEffective(1000L);
        encryptionConfig.setPosition("header");
        encryptionConfig.setTokenName("token");
        jwtConfig.setTokenCreateMode(TokenCreateMode.JWT);
        jwtConfig.setAlgorithm("AES");
        jwtConfig.setKey("1234567890123456");
        jwtConfig.setEffective(1000L);
        jwtConfig.setPosition("header");
        jwtConfig.setTokenName("token");
    }

    @Test
    public void UUidTest(){
        TokenCreateService tokenCreateService = TokenService.createTokenService(uuidConfig);
        Assert.assertEquals(32,tokenCreateService.createToken(null).length());
    }

    @Test
    public void RandomTest(){
        TokenCreateService tokenCreateService = TokenService.createTokenService(randomConfig);
        Assert.assertEquals(32,tokenCreateService.createToken(null).length());
    }

}
