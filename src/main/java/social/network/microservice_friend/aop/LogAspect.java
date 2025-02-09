
package social.network.microservice_friend.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LogAspect {
    @Before("@annotation(com.example.annotations.Logger)")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String message = String.format("Метод %s вызван с аргументами: %s", methodName, Arrays.toString(args));
        log.info(message);
    }
}














