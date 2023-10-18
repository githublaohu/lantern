package com.lamp.lantern.plugins.api.service;

public interface ThirdAuthService extends AuthService{
    public RedirectAddress getRedirectAddress();




    class RedirectAddress{
        String url;
    }
}
