package com.lamp.lantern.plugins.springmvc.support;

import com.lamp.lantern.plugins.api.annotation.Injection;
import com.lamp.lantern.plugins.api.annotation.OrganizationInjection;
import com.lamp.lantern.plugins.api.annotation.UserInjection;
import com.lamp.lantern.plugins.core.login.LanternContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * @author laohu
 */
public class LanternRequestResponseBodyMethodProcessor implements HandlerMethodArgumentResolver {

    private final RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;
    private final Map<Class<?>, List<MapperInfo>> classMap = new ConcurrentHashMap<>();

    public LanternRequestResponseBodyMethodProcessor(
            RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor) {
        this.requestResponseBodyMethodProcessor = requestResponseBodyMethodProcessor;
    }

    @Override
    public boolean supportsParameter(@NotNull MethodParameter parameter) {
        return requestResponseBodyMethodProcessor.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        Object object =
                requestResponseBodyMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (Objects.isNull(object)) {
            return object;
        }
        Object value;
        if (object instanceof Optional) {
            if (Objects.equals(Optional.empty(), object)) {
                return object;
            }
            value = ((Optional<?>) object).get();
        } else {
            value = object;
        }
        List<MapperInfo> mappingList = classMap.get(value.getClass());
        if (Objects.isNull(mappingList)) {
            mappingList = new ArrayList<>();
            classMap.put(value.getClass(), mappingList);
            Class<?> clazz = value.getClass();
            while (!Objects.equals(clazz, Object.class)) {
                Field[] declaredFields = new Field[0];
                if (clazz != null) {
                    declaredFields = clazz.getDeclaredFields();
                    clazz = clazz.getSuperclass();
                }
                for (Field field : declaredFields) {
                    UserInjection userInjection = field.getAnnotation(UserInjection.class);
                    OrganizationInjection organizationInjection = field.getAnnotation(OrganizationInjection.class);
                    Injection injection = field.getAnnotation(Injection.class);
                    MapperInfo mapperInfo = new MapperInfo();
                    String valueInjection = null;
                    if (Objects.nonNull(userInjection)) {
                        valueInjection = userInjection.value();
                        mapperInfo.lanternObject = "userInfo";
                    } else if (Objects.nonNull(organizationInjection)) {
                        valueInjection = organizationInjection.value();
                        mapperInfo.lanternObject = "organization";
                    } else if (Objects.nonNull(injection)) {
                        mapperInfo.lanternObject = injection.value();
                        valueInjection = injection.fieldName();
                    }
                    if (Objects.isNull(valueInjection) || Objects.equals(valueInjection, "")) {
                        throw new RuntimeException("");
                    }
                    field.setAccessible(true);
                    mapperInfo.targetField = field;
                    mapperInfo.lanternObjectFieldName = valueInjection;
                    mappingList.add(mapperInfo);
                }
            }
        }
        if (mappingList.isEmpty()) {
            return object;
        }
        for (MapperInfo mapperInfo : mappingList) {
            Object lanternObject = 0;
            if (Objects.equals("userInfo", mapperInfo.lanternObject)) {
                lanternObject = LanternContext.getContext().getUserInfo();
            } else {
                lanternObject = LanternContext.getContext().getValue(mapperInfo.lanternObject);
            }
            // 得到数据
            if (Objects.isNull(mapperInfo.lanternObjectField)) {
                mapperInfo.lanternObjectField = lanternObject.getClass().getField(mapperInfo.lanternObjectFieldName);
                mapperInfo.lanternObjectField.setAccessible(true);
            }
            Object valueInjection = mapperInfo.lanternObjectField.get(lanternObject);
            mapperInfo.targetField.set(value, valueInjection);
        }
        return object;
    }

    static class MapperInfo {

        private String lanternObject;

        private Field lanternObjectField;

        private String lanternObjectFieldName;

        private Class<?> targetClass;

        private Field targetField;

    }
}
