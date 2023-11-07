package com.lamp.lantern.plugins.api.auth;

import java.util.Map;

import com.lamp.lantern.plugins.api.mode.UserInfo;
import lombok.Data;

/**
 * @author laohu
 */
@Data
public class AuthenticationServiceResult implements java.io.Serializable{

    /**
     * 用户
     */
    private UserInfo userInfo;


    /**
     * 企业
     */
    private Object enterprise;

    /**
     * 部门信息
     */
    private Object department;

    private Map<String, Object>  object;

    private String errorMessage;

    private boolean success;

    public AuthenticationServiceResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
