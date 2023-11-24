package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.LanternAuthCachePool;
import com.lamp.lantern.plugins.api.auth.config.DBAuthenticationDataConfig;
import com.lamp.lantern.plugins.api.mode.Role;
import com.lamp.lantern.service.core.provider.mapper.ResourcesMapper;
import com.lamp.lantern.service.core.provider.mapper.RoleMapper;
import com.lamp.lantern.service.core.provider.mapper.RoletypeRoleRelationMapper;
import com.lamp.lantern.service.core.provider.mapper.UserRoleRelationMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author laohu
 */
@Slf4j
@Service
public class DBAuthenticationDataService implements AuthenticationDataService {

    private final ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(255, 511, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    private final DirectDBAuthenticationDataService directDBAuthenticationDataService =
            new DirectDBAuthenticationDataService();
    private Map<String, DBAuthenticationDataConfig> dbAuthenticationDataConfigMap = new ConcurrentHashMap<>();

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(255,511,10, TimeUnit.SECONDS , new LinkedBlockingQueue<>());

    @PostConstruct
    public void init() {

    }


    //@Scheduled()
    public void increment() {

    }

    @Override
    public LanternAuthCachePool getLanternAuthCachePool(AuthenticationData authenticationData) {
        if (Objects.isNull(authenticationData.getProductId())) {

        }
        if (authenticationData.getTime() == 0) {

        }

        return this.directDBAuthenticationDataService.getLanternAuthCachePool(authenticationData);
    }


    class DirectDBAuthenticationDataService implements AuthenticationDataService {

        @Override
        public LanternAuthCachePool getLanternAuthCachePool(AuthenticationData authenticationData) {
            try {
                // 通过 SQL 用区别全量与增量
                CompletableFuture<List<Role>> roleAsync =
                        CompletableFuture.supplyAsync(() -> roleMapper.selectRoles(), threadPoolExecutor);

                roleAsync.get();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            return null;
        }
    }

    static class CacheDBAuthenticationDataService implements AuthenticationDataService {

        @Override
        public LanternAuthCachePool getLanternAuthCachePool(AuthenticationData authenticationData) {
            return null;
        }
    }
}
