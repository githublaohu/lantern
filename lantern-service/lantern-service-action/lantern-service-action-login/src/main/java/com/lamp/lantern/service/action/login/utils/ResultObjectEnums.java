package com.lamp.lantern.service.action.login.utils;

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

    PASSWORD_ERROR(600, "password is wrong"),


    CHECK_PARAMETERS_FAIL(120001, "check parameters fail");

    private ResultObject<String> resultObject;

    ResultObjectEnums(Integer code, String message){
        this.resultObject = ResultObject.getResultObjectMessgae(code,message);
    }

    public ResultObject<String> getResultObject(){
        return this.resultObject;
    }
}
