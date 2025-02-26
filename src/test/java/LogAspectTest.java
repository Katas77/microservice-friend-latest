
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import social.network.microservice_friend.aop.LogAspect;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;


public class LogAspectTest {

    private Logger mockLogger;
    private LogAspect aspect;

    @BeforeEach
    public void setUp() {
        mockLogger = Mockito.mock(Logger.class);
        aspect = new LogAspect(mockLogger);
    }

    @Test
    public void testLogMethodCall() throws Throwable {
        Signature mockSignature = Mockito.mock(Signature.class);
        JoinPoint mockJoinPoint = Mockito.mock(JoinPoint.class);

        when(mockJoinPoint.getSignature()).thenReturn(mockSignature);
        when(mockSignature.getName()).thenReturn("testMethod");
        when(mockJoinPoint.getArgs()).thenReturn(new Object[] { "arg1", "arg2" });

        aspect.logMethodCall(mockJoinPoint);

        verify(mockLogger).info("Метод testMethod вызван с аргументами: [arg1, arg2]");
    }

    @Test
    public void testLogExecutionTime() throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature mockSignature = Mockito.mock(Signature.class);

        when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
        when(mockSignature.getName()).thenReturn("testMethod");
        when(proceedingJoinPoint.proceed()).thenReturn(null);

        aspect.logExecutionTime(proceedingJoinPoint);

        verify(mockLogger).info(eq("Время выполнения метода {} составило {} мс."));
    }
}