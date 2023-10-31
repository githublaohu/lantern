package com.lamp.lantern.plugins.api.auth;

import java.util.Map;
import lombok.Data;

/**
 * @author laohu
 */
@Data
public class AuthenticationServiceResult {

    /**
     * 用户
     */
    private Object userInfo;


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


}
