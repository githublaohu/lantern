package com.lamp.lantern.plugins.api.auth;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laohu
 */
public class LanternAuthCachePool {


    private volatile Map<String,Object> tokenToUserMap = new ConcurrentHashMap<>();

    private volatile Map<String, List<Object>>  userAndRoleMap = new ConcurrentHashMap<>();

    private volatile Map<String,Object>    roleMapMap = new ConcurrentHashMap<>();

    private volatile Map<String,List<Object>> roleAndResourceMap = new ConcurrentHashMap<>();

    private volatile Map<String , Object> resourceMap = new ConcurrentHashMap<>();


    public Map<String, Object> getTokenToUserMap() {
        return tokenToUserMap;
    }

    public void setTokenToUserMap(Map<String, Object> tokenToUserMap) {
        this.tokenToUserMap = tokenToUserMap;
    }

    public Map<String, List<Object>> getUserAndRoleMap() {
        return userAndRoleMap;
    }

    public void setUserAndRoleMap(Map<String, List<Object>> userAndRoleMap) {
        this.userAndRoleMap = userAndRoleMap;
    }

    public Map<String, Object> getRoleMapMap() {
        return roleMapMap;
    }

    public void setRoleMapMap(Map<String, Object> roleMapMap) {
        this.roleMapMap = roleMapMap;
    }

    public Map<String, List<Object>> getRoleAndResourceMap() {
        return roleAndResourceMap;
    }

    public void setRoleAndResourceMap(Map<String, List<Object>> roleAndResourceMap) {
        this.roleAndResourceMap = roleAndResourceMap;
    }

    public Map<String, Object> getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map<String, Object> resourceMap) {
        this.resourceMap = resourceMap;
    }
}
