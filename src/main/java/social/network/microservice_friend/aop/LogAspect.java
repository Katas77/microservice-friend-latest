
package social.network.microservice_friend.aop;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LogAspect {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    @AfterThrowing("@annotation(LoggerThrowing)")
    public void account(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof UUID) {
                {log.info("Account with ID {}  is NOT_FOUND", object);}
            }
    }}

    @Before("@annotation(Logger)")
    public void searchDto(JoinPoint joinPoint) {
        String name=joinPoint.getSignature().getName();
        System.out.println(ANSI_BLUE + "Warning ! Method  {} is    calling"+name + ANSI_RESET);
        Object[] objects = joinPoint.getArgs();
        Arrays.stream(objects).filter(o -> !o.toString().startsWith("Bearer")).forEach(o -> System.out.println(o.toString()+"     -  parameters" ));
    }

}














