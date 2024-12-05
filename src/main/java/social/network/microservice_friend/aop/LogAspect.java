
package social.network.microservice_friend.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import social.network.microservice_friend.dto.SearchDto;


import java.util.UUID;


@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LogAspect {


    @AfterThrowing("@annotation(LoggerThrowing)")
    public void account(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof UUID) {
                {
                    log.info("Account with ID {0} is NOT_FOUND", object);
                }
                if (object instanceof Exception) {
                    {
                        log.info(object.toString());
                    }
            }
        }

    }}

    @Before("@annotation(Logger)")
    public void searchDto(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof SearchDto) {
                {
                    log.info(object.toString());
                }
            }
        }

    }
}













