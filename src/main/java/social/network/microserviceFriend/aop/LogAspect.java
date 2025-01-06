
package social.network.microserviceFriend.aop;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LogAspect {
    @Before("@annotation(Logger)")
    public void methodName(JoinPoint joinPoint) {
        String name=joinPoint.getSignature().getName();
        log.info("Warning ! Method  {} is    calling   ",name);
    }
}














