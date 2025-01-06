package social.network.microservice_friend;

import net.javacrumbs.jsonunit.JsonAssert;
import org.aspectj.lang.JoinPoint;
import org.hibernate.NonUniqueResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import social.network.microservice_friend.aop.LogAspect;
import social.network.microservice_friend.clientFeign.ClientFeign;
import social.network.microservice_friend.configuration.config_security.RequestHeaderAuthenticationProvider;
import social.network.microservice_friend.dto.Message;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.exception.FriendExceptionHandler;
import social.network.microservice_friend.test_utils.UtilsT;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static org.mockito.Mockito.verify;

public class OwlOnGlobeTests {
    ClientFeign accountClient = Mockito.mock(ClientFeign.class);
    FriendExceptionHandler friend = new FriendExceptionHandler();

    @DisplayName("Test for readStringFrom")
    @Test
    void readStringFromResource() {
        Message expectedResponse = Message.builder().report("Friendship with uuidTo b3999ffa-2df9-469e-9793-ee65e214846e is approve").build();
        String actualResponse = UtilsT.readStringFromResource("response/approve.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test for LogAspect")
    @Test
    void LogAspect() {
        LogAspect logAspect = Mockito.mock(LogAspect.class);
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        logAspect.methodName(joinPoint);
        Mockito.verify(logAspect, Mockito.times(1)).methodName(joinPoint);
        verify(logAspect).methodName(joinPoint);
    }

    @DisplayName("Test for RequestHeaderAuthenticationProvider")
    @Test
    void RequestHeaderAuthenticationProvider() {
        String mes = "";
        RequestHeaderAuthenticationProvider provider = new RequestHeaderAuthenticationProvider(accountClient);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(UtilsT.token);
        try {
            provider.authenticate(authentication);
        } catch (BadCredentialsException e) {
            mes = e.getMessage();
        }
        Assertions.assertEquals("Bad Request invalid or missing token", mes);
    }

    @DisplayName("Test for FriendExceptionHandler")
    @Test
    void FriendExceptionHandler() {
        BusinessLogicException e = new BusinessLogicException("BusinessLogicException is    calling");
        String actual = Objects.requireNonNull(friend.handleException(e).getBody()).getReport();
        Assertions.assertEquals("BusinessLogicException is    calling", actual);
    }

    @DisplayName("Test for FriendExceptionHandler")
    @Test
    void handleExceptionNull() {
        NullPointerException e = new NullPointerException("NullPointerException is    calling");
        String actual = Objects.requireNonNull(friend.handleExceptionNull(e).getBody()).getReport();
        Assertions.assertEquals("NullPointerException is    calling", actual);
    }

    @DisplayName("Test for IllegalArgumentException")
    @Test
    void IllegalArgumentExceptionF() {
        IllegalArgumentException e = new IllegalArgumentException("IllegalArgumentException is    calling");
        String actual = Objects.requireNonNull(friend.handleExceptionIllegalA(e).getBody()).getReport();
        Assertions.assertEquals("IllegalArgumentException is    calling", actual);
    }

    @DisplayName("Test for FriendExceptionHandler")
    @Test
    void FriendHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException e = new HttpRequestMethodNotSupportedException("HttpRequestMethodNotSupportedException is    calling");
        String actual = Objects.requireNonNull(friend.handleException(e).getBody()).getReport();
        Assertions.assertEquals("Request method 'HttpRequestMethodNotSupportedException is    calling' is not supported", actual);
    }


    @DisplayName("Test for FriendMissingServletRequestParameterException")
    @Test
    void FriendMissingServletRequestParameterException() {
        MissingServletRequestParameterException e = new MissingServletRequestParameterException(null, null);
        String actual = Objects.requireNonNull(friend.handleExceptionServlet(e).getBody()).getReport();
        Assertions.assertEquals("Required request parameter 'null' for method parameter type null is not present", actual);
    }

    @DisplayName("Test for FriendMissingServletRequestParameterException")
    @Test
    void FriendNonUniqueResultException() {
        NonUniqueResultException e = new NonUniqueResultException(1);
        String actual = Objects.requireNonNull(friend.handleExceptionServlet(e).getBody()).getReport();
        Assertions.assertEquals("Query did not return a unique result: 1 results were returned", actual);
    }
    @DisplayName("Test for FriendMissingPathVariableException")
    @Test
    void FriendMissingPathVariableException () {
        Method testMethod = Arrays.stream(getClass().getMethods()).findFirst().orElseThrow();
        MissingPathVariableException e = new MissingPathVariableException("variableName",new MethodParameter(testMethod, 0));
        String actual = Objects.requireNonNull(friend.handleExceptionPath(e).getBody()).getReport();
        Assertions.assertEquals("Required URI template variable 'variableName' for method parameter type Object is not present", actual);
    }


}
