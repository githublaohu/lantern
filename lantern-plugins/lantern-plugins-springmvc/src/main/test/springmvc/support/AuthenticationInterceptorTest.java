package springmvc.support;


import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.authentication.AuthenticationManager;
import com.lamp.lantern.plugins.core.login.LanternContext;
import com.lamp.lantern.plugins.springmvc.support.AuthenticationInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hahaha
 */
public class AuthenticationInterceptorTest {
    private AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    private AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(authenticationManager);
    @Before
    public void init(){
        Mockito.doAnswer(invocationOnMock -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setUiToken("123");
            AuthenticationServiceResult authenticationServiceResult = new AuthenticationServiceResult().setSuccess(true);
            authenticationServiceResult.setUserInfo(userInfo);
            return authenticationServiceResult;
        }).when(authenticationManager).authentication(Mockito.any(HttpServletRequest.class),Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void test() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Object handler = new Object();
        authenticationInterceptor.preHandle(request,response,handler);
        Object userInfo = LanternContext.getContext();
        Assert.assertNotNull(LanternContext.getContext().getUserInfo());
    }
}