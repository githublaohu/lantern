package com.lamp.lantern.service.action.login.security;

public interface WxSignatureCheckService {

    public String wxSignatureCheck(String signature, String timestamp, String nonce, String echostr);

    public String sort(String token, String timestamp, String nonce);
}
