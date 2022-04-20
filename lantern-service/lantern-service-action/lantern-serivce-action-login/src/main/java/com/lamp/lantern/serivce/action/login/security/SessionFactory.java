package com.lamp.lantern.serivce.action.login.security;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.lamp.lantern.service.core.entity.UserInfoEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionFactory {

    private static final SessionFactory SESSION_FACTORY = new SessionFactory();

    public static final SessionFactory getInstance(){
        return SESSION_FACTORY;
    }

    private final Map<Class<?>, Map<Object, Object>> cacheObject = new ConcurrentHashMap<>();

    private SessionFactory() {
    }

    @SuppressWarnings("unchecked")
    public void setCache(Object key, Object object) {
        Class<?> clazz = null;
        if (object instanceof List) {
            List<Object> list = ((List<Object>) object);
            if (list.isEmpty()) {
                return;
            }
            clazz = list.get(0).getClass();
        }
        if (object instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            if (map.isEmpty()) {
                return;
            }
            clazz = map.values().iterator().next().getClass();
        } else {
            clazz = object.getClass();
        }
        this.setCache(key, object, clazz);
    }

    public void setCache(Object key, Object object, Class<?> clazz) {
        Map<Object, Object> map = cacheObject.get(clazz);
        if (Objects.isNull(map)) {
            map = cacheObject.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        }
        map.put(key, object);
    }

    public void deleteCache(Object key, Class<?> clazz) {
        Map<Object, Object> map = cacheObject.get(clazz);
        if (Objects.isNull(map)) {
            map = cacheObject.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        }
        map.remove(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCache(Object key, Class<?> clazz) {
        Map<Object, Object> map = cacheObject.get(clazz);
        if (Objects.isNull(map)) {
            map = cacheObject.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        }
        return (T) map.get(key);
    }

    public UserInfoEntity getUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest();
        String authorization = request.getHeader("Authorization");
        return getCache(authorization, UserInfoEntity.class);

    }

}

