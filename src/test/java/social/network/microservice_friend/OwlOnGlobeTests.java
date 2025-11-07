package social.network.microservice_friend;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.JsonAssert;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.NonUniqueResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import social.network.microservice_friend.aop.LogAspect;
import social.network.microservice_friend.feigns.ClientFeign;
import social.network.microservice_friend.configuration.security.RequestHeaderAuthenticationProvider;
import social.network.microservice_friend.dto.Message;
import social.network.microservice_friend.exception.BusinessLogicException;
import social.network.microservice_friend.exception.FriendExceptionHandler;
import social.network.microservice_friend.test_utils.UtilsT;

import java.util.Objects;

class OwlOnGlobeTests {
    private final ClientFeign accountClient = Mockito.mock(ClientFeign.class);
    private final FriendExceptionHandler friend = new FriendExceptionHandler();
    private final ObjectMapper mapper = new ObjectMapper();

    @DisplayName("Read string from resource and compare JSON")
    @Test
    void readStringFromResource() throws Exception {
        Message expectedResponse = Message.builder()
                .report("Friendship with uuidTo b3999ffa-2df9-469e-9793-ee65e214846e is approve")
                .build();

        String actualResponse = UtilsT.readStringFromResource("response/approve.json");

        String expectedJson = mapper.writeValueAsString(expectedResponse);
        JsonAssert.assertJsonEquals(expectedJson, actualResponse);
    }

    @DisplayName("LogAspect: method call (mock verification)")
    @Test
    void logAspectMethodCall() {
        LogAspect logAspect = Mockito.mock(LogAspect.class);
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);

        logAspect.logMethodCall(joinPoint);
        Mockito.verify(logAspect, Mockito.times(1)).logMethodCall(joinPoint);
    }

    @DisplayName("RequestHeaderAuthenticationProvider should throw BadCredentialsException on invalid token")
    @Test
    void requestHeaderAuthenticationProviderThrows() {
        RequestHeaderAuthenticationProvider provider = new RequestHeaderAuthenticationProvider(accountClient);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(UtilsT.token);

        String message = Assertions.assertThrows(BadCredentialsException.class,
                        () -> provider.authenticate(authentication))
                .getMessage();

        Assertions.assertEquals("Bad Request invalid or missing token", message);
    }

    @DisplayName("FriendExceptionHandler handles BusinessLogicException")
    @Test
    void friendExceptionHandlerBusinessLogic() {
        BusinessLogicException e = new BusinessLogicException("BusinessLogicException is    calling");
        String actual = Objects.requireNonNull(friend.handleException(e).getBody()).getReport();
        Assertions.assertEquals("BusinessLogicException is    calling", actual);
    }

    @DisplayName("FriendExceptionHandler handles NullPointerException")
    @Test
    void handleExceptionNull() {
        NullPointerException e = new NullPointerException();
        String actual = Objects.requireNonNull(friend.handleExceptionNull(e).getBody()).getReport();
        Assertions.assertEquals("Null Pointer Exception", actual);
    }

    @DisplayName("FriendExceptionHandler handles IllegalArgumentException")
    @Test
    void illegalArgumentExceptionHandler() {
        IllegalArgumentException e = new IllegalArgumentException();
        String actual = Objects.requireNonNull(friend.handleExceptionIllegalA(e).getBody()).getReport();
        Assertions.assertEquals("Illegal Argument Exception", actual);
    }

    @DisplayName("FriendExceptionHandler handles HttpRequestMethodNotSupportedException")
    @Test
    void friendHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException e = new HttpRequestMethodNotSupportedException("HTTP Method Supported ");
        String actual = Objects.requireNonNull(friend.handleException(e).getBody()).getReport();
        Assertions.assertEquals("HTTP Method Not Supported Exception", actual);
    }

    @DisplayName("FriendExceptionHandler handles MissingServletRequestParameterException")
    @Test
    void friendMissingServletRequestParameterException() {
        MissingServletRequestParameterException e = new MissingServletRequestParameterException("param", "type");
        String actual = Objects.requireNonNull(friend.handleExceptionServlet(e).getBody()).getReport();
        Assertions.assertEquals("Missing Servlet Request Parameter Exception", actual);
    }

    @DisplayName("FriendExceptionHandler handles NonUniqueResultException")
    @Test
    void friendNonUniqueResultException() {
        NonUniqueResultException e = new NonUniqueResultException(1);
        String actual = Objects.requireNonNull(friend.handleExceptionServlet(e).getBody()).getReport();
        Assertions.assertEquals("Non Unique Result Exception", actual);
    }

    @DisplayName("LogAspect: logExecutionTime (mock verification)")
    @Test
    void testLogExecutionTime() throws Throwable {
        LogAspect logAspect = Mockito.mock(LogAspect.class);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);

        logAspect.logExecutionTime(joinPoint);
        Mockito.verify(logAspect, Mockito.times(1)).logExecutionTime(joinPoint);
    }
}
