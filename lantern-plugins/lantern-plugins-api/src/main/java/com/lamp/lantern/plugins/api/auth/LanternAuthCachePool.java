package com.lamp.lantern.plugins.api.auth;

import com.lamp.lantern.plugins.api.mode.Resources;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laohu
 */
@Data
public class LanternAuthCachePool {

    private volatile Map<String, UserInfo> tokenUserMap = new ConcurrentHashMap<>();

    private volatile Map<Long, List<Long>> userRoleMap = new ConcurrentHashMap<>();

    private volatile Map<Long, List<Long>> roleResourceMap = new ConcurrentHashMap<>();

    private volatile Map<Long, Role> roleMap = new ConcurrentHashMap<>();

    private volatile Map<Long, Resources> resourceMap = new ConcurrentHashMap<>();


}
