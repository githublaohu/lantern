package springmvc.support;

import com.lamp.lantern.plugins.api.annotation.OrganizationInjection;
import com.lamp.lantern.plugins.api.annotation.UserInjection;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.springmvc.support.LanternRequestResponseBodyMethodProcessor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * @author lambert
 */
public class LanternRequestResponseBodyMethodProcessorTest {
    
    private RequestResponseBodyMethodProcessor defaultProcessor;

    
    private NativeWebRequest webRequest;

    
    private WebDataBinderFactory binderFactory;

    
    private ModelAndViewContainer modelAndViewContainer;

    private LanternRequestResponseBodyMethodProcessor processor;

    private MethodParameter parameter;


    @Before
    public void setUp() throws Exception {
        defaultProcessor = Mockito.mock(RequestResponseBodyMethodProcessor.class);
        Mockito.when(defaultProcessor.supportsParameter(Mockito.any(MethodParameter.class))).thenReturn(true);

        parameter = Mockito.mock(MethodParameter.class);

        Mockito.when(parameter.hasParameterAnnotation(RequestBody.class)).thenReturn(true);

        // 模拟登录上下文
        UserInfo userInfo = new UserInfo();
        userInfo.setUiId(1L);
        userInfo.setUiName("test");
        LanternContext.getContext().setUserInfo(userInfo);
        LanternContext.getContext().setValue("organization", new Object());

        webRequest = Mockito.mock(NativeWebRequest.class);
        binderFactory = Mockito.mock(WebDataBinderFactory.class);
        modelAndViewContainer = Mockito.mock(ModelAndViewContainer.class);

    }

    @Test
    public void supportTest() throws Exception {
        Mockito.when(defaultProcessor.resolveArgument(Mockito.any(MethodParameter.class),
                Mockito.any(ModelAndViewContainer.class), Mockito.any(NativeWebRequest.class),
                Mockito.any(WebDataBinderFactory.class))).thenReturn(new Object());

        processor = new LanternRequestResponseBodyMethodProcessor(defaultProcessor);

        Assert.assertTrue(processor.supportsParameter(parameter));
    }

    @Test
    public void resolveArgumentNullTest() throws Exception {
        Mockito.when(defaultProcessor.resolveArgument(Mockito.any(MethodParameter.class),
                Mockito.any(ModelAndViewContainer.class), Mockito.any(NativeWebRequest.class),
                Mockito.any(WebDataBinderFactory.class))).thenReturn(null);

        processor = new LanternRequestResponseBodyMethodProcessor(defaultProcessor);

        Object object = processor.resolveArgument(parameter, modelAndViewContainer, webRequest, binderFactory);
        Assert.assertNull(object);
    }

    @Test
    public void resolveArgumentClassTest() throws Exception {
        Mockito.when(defaultProcessor.resolveArgument(Mockito.any(MethodParameter.class),
                Mockito.any(ModelAndViewContainer.class), Mockito.any(NativeWebRequest.class),
                Mockito.any(WebDataBinderFactory.class))).thenReturn(new Object());

        processor = new LanternRequestResponseBodyMethodProcessor(defaultProcessor);

        Object object = processor.resolveArgument(parameter, modelAndViewContainer, webRequest, binderFactory);
        Assert.assertNotNull(object);
    }

    @Test
    public void resolveArgumentUserDTOTest() throws Exception {
        Mockito.when(defaultProcessor.resolveArgument(Mockito.any(MethodParameter.class),
                Mockito.any(ModelAndViewContainer.class), Mockito.any(NativeWebRequest.class),
                Mockito.any(WebDataBinderFactory.class))).thenReturn(new UserDTO());

        processor = new LanternRequestResponseBodyMethodProcessor(defaultProcessor);

        Object object = processor.resolveArgument(parameter, modelAndViewContainer, webRequest, binderFactory);
        Assert.assertNotNull(object);
        Assert.assertEquals(object.getClass(), UserDTO.class);
        Assert.assertEquals(((UserDTO) object).getId(), Long.valueOf(1L));
        Assert.assertEquals(((UserDTO) object).getName(), "test");
    }

    @Data
    class UserDTO {

        @UserInjection("uiId")
        private Long id;

        @UserInjection("uiName")
        private String name;

        // getter/setter

    }
}
