package social.network.microservice_friend.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import social.network.microservice_friend.dto.Message;

@ControllerAdvice
@Slf4j
public class FriendExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Message> handleException(BusinessLogicException e) {
        log.error("BusinessLogicException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.builder()
                .report(e.getMessage())
                .build());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Message> handleExceptionNull(NullPointerException e) {
        log.error("NullPointerException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
                .report("Null Pointer Exception")
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Message> handleExceptionIllegalA(IllegalArgumentException e) {
        log.error("IllegalArgumentException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
                .report("Illegal Argument Exception")
                .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Message> handleException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
                .report("HTTP Method Not Supported Exception")
                .build());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Message> handleExceptionFeignException(FeignException e) {
        log.error("FeignException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
                .report("Feign Exception")
                .build());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Message> handleExceptionPath(MissingPathVariableException e) {
        log.error("MissingPathVariableException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
                .report("Missing Path Variable Exception")
                .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Message> handleExceptionServlet(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
                .report("Missing Servlet Request Parameter Exception")
                .build());
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<Message> handleExceptionServlet(NonUniqueResultException e) {
        log.error("NonUniqueResultException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.builder()
                .report("Non Unique Result Exception")
                .build());
    }

}