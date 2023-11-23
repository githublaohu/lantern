package com.lamp.lantern.service.core.provider.service.impl;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import com.lamp.lantern.plugins.api.auth.LanternAuthCachePool;
import com.lamp.lantern.plugins.api.auth.config.DBAuthenticationDataConfig;
import com.lamp.lantern.plugins.api.auth.config.DBAuthenticationDataModel;
import com.lamp.lantern.service.core.provider.mapper.ResourcesMapper;
import com.lamp.lantern.service.core.provider.mapper.RoleMapper;
import com.lamp.lantern.service.core.provider.mapper.RoletypeRoleRelationMapper;
import com.lamp.lantern.service.core.provider.mapper.UserRoleRelationMapper;
import java.util.HashMap;
import java.util.Map;
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

    private Map<DBAuthenticationDataModel, AuthenticationDataService>  authenticationDataServiceMap = new HashMap<>();

    private Map<String, DBAuthenticationDataConfig> dbAuthenticationDataConfigMap = new ConcurrentHashMap<>();

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoletypeRoleRelationMapper roletypeRoleRelationMapper;


    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(255,511,10, TimeUnit.SECONDS , new LinkedBlockingQueue<>())

    @PostConstruct
    public void init(){
        authenticationDataServiceMap.put(DBAuthenticationDataModel.FULL , new FullDBAuthenticationDataService());

        resourcesMapper.selectResources();
        roleMapper.selectRoles();
        userRoleRelationMapper.selectUserRoleRelations();
    }


    @Override
    public LanternAuthCachePool getLanternAuthCachePool(AuthenticationData authenticationData) {

        return null;
    }

    class FullDBAuthenticationDataService implements AuthenticationDataService {

        @Override
        public LanternAuthCachePool getLanternAuthCachePool(AuthenticationData authenticationData) {


            return null;
        }
    }

    class FullCacheAuthenticationDataService  implements AuthenticationDataService{

        @Override
        public LanternAuthCachePool getLanternAuthCachePool(AuthenticationData authenticationData) {
            return null;
        }
    }
}
