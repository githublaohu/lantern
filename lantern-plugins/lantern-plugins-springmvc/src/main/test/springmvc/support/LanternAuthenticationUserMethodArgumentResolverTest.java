package springmvc.support;

import com.lamp.lantern.plugins.api.annotation.Injection;
import com.lamp.lantern.plugins.api.annotation.OrganizationInjection;
import com.lamp.lantern.plugins.api.annotation.UserInjection;
import com.lamp.lantern.plugins.api.mode.OrganizationInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.springmvc.support.LanternAuthenticationUserMethodArgumentResolver;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author hahaha
 */
public class LanternAuthenticationUserMethodArgumentResolverTest{
    private final LanternAuthenticationUserMethodArgumentResolver lanternAuthenticationUserMethodArgumentResolver = new LanternAuthenticationUserMethodArgumentResolver();

    private final MethodParameter methodParameter1 = Mockito.mock(MethodParameter.class);
    private final MethodParameter methodParameter2 = Mockito.mock(MethodParameter.class);
    private final MethodParameter methodParameter3 = Mockito.mock(MethodParameter.class);

    @Before
    public void setUp() throws Exception {

        Mockito.when(methodParameter1.hasParameterAnnotation(UserInjection.class)).thenReturn(true);
        Mockito.when(methodParameter2.hasParameterAnnotation(OrganizationInjection.class)).thenReturn(true);
        Mockito.when(methodParameter3.hasParameterAnnotation(Injection.class)).thenReturn(true);

    }

    @Test
    public void supportsParameterTest() {
        Assert.assertTrue(lanternAuthenticationUserMethodArgumentResolver.supportsParameter(methodParameter1));
        Assert.assertTrue(lanternAuthenticationUserMethodArgumentResolver.supportsParameter(methodParameter2));
        Assert.assertTrue(lanternAuthenticationUserMethodArgumentResolver.supportsParameter(methodParameter3));
    }

    @Test
    public void resolveArgumentTest() {
        NativeWebRequest nativeWebRequest = Mockito.mock(NativeWebRequest.class);
        WebDataBinderFactory webDataBinderFactory = Mockito.mock(WebDataBinderFactory.class);
        ModelAndViewContainer modelAndViewContainer = Mockito.mock(ModelAndViewContainer.class);

        LanternContext.getContext().setUserInfo(new Object());
        Assert.assertNotNull(lanternAuthenticationUserMethodArgumentResolver.resolveArgument(methodParameter1,modelAndViewContainer,nativeWebRequest,webDataBinderFactory));

        LanternContext.getContext().setValue("organization",new OrganizationInfo());
        Mockito.doReturn(OrganizationInfo.class).when(methodParameter2).getParameterType();
        Assert.assertNotNull(lanternAuthenticationUserMethodArgumentResolver.resolveArgument(methodParameter2,modelAndViewContainer,nativeWebRequest,webDataBinderFactory));

        Injection injection = Mockito.mock(Injection.class);
        Mockito.doReturn(injection).when(methodParameter3).getParameterAnnotation(Injection.class);
        Mockito.doReturn("organization").when(injection).value();
        Assert.assertNotNull(lanternAuthenticationUserMethodArgumentResolver.resolveArgument(methodParameter3,modelAndViewContainer,nativeWebRequest,webDataBinderFactory));

    }
}
