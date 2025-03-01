package social.network.microservice_friend.aop;

import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LogAspect {
    private final Logger logger;

    public LogAspect() {
        this.logger = LoggerFactory.getLogger(LogAspect.class);
    }

    public LogAspect(Logger logger) {
        this.logger = logger;
    }

    @Before("@annotation(com.example.annotations.Logger)")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String message = String.format("Метод %s вызван с аргументами: %s", methodName, Arrays.toString(args));
        logger.info(message);
    }
    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object proceed = joinPoint.proceed();
        long executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        String message = String.format("Время выполнения метода {} составило {} мс." ,joinPoint.getSignature().getName(), executionTime);
        logger.info(message);
        return proceed;
    }

}














