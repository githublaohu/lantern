package com.lamp.lantern.service.action.user.utils;

import com.lamp.decoration.core.result.ResultObject;

public enum ResultObjectEnums {

    SUCCESS(200,"success"),

    FAIL(500,"fail"),

    USERNAME_ERROR(501, "username is not exist"),

    PHONE_ERROR(502, "phone number is not exist"),

    EMAIL_ERROR(503, "email is not exist"),

    VERIFY_CODE_EXPIRE(504, "verify code has expired"),

    VERIFY_CODE_WRONG(505, "verify code is wrong"),

    TOKEN_ERROR(506, "token is wrong"),

    LOGIN_ERROR_OVERRUN(507, "login error times exceed 5 time"),

    USERNAME_HAVE_BEEN_REGISTER(508, "username have been register"),

    PHONE_HAVE_BEEN_REGISTER(509, "phone have been register"),

    EMAIL_HAVE_BEEN_REGISTER(510, "email have been register"),

    USERNAME_MUST_NOT_BLANK(511, "username must not be blank"),

    PHONE_MUST_NOT_BLANK(512, "phone must not be blank"),

    EMAIL_MUST_NOT_BLANK(513, "email must not be blank"),

    WECHAT_REDIRECT_FAIL(514, "wechar redirect url failed"),

    WECHAT_SIGNATURE_ERROR(515, "wechat signature error"),

    WECHAT_SCAN_LOGIN_ERROR(516, "wechat scan login error"),

    PASSWORD_ERROR(600, "password is wrong"),

    CHANGE_PASSWORD_SAME_AS_OLD(601, "change password is same as old password"),

    CHANGE_NICKNAME_SAME_AS_OLD(602, "change nickname is same as old password"),

    UNKONWN_ERROR(700, "have encounter unknown error"),





    CHECK_PARAMETERS_FAIL(120001, "check parameters fail");

    private ResultObject<String> resultObject;

    ResultObjectEnums(Integer code, String message){
        this.resultObject = ResultObject.getResultObjectMessgae(code,message);
    }

    public void setResultObjectMessage(String errorMessage){
        this.resultObject.setMessage(errorMessage);
    }

    public ResultObject<String> getResultObject(){
        return this.resultObject;
    }
}
