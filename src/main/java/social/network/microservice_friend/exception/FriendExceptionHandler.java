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

@ControllerAdvice
@Slf4j
public class FriendExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<String> handleException(BusinessLogicException e) {
        log.error("BusinessLogicException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleExceptionNull(NullPointerException e) {
        log.error("NullPointerException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Что-то пошло не так, задайте параметры поиска");

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleExceptionIllegalA(IllegalArgumentException e) {
        log.error("IllegalArgumentException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleExceptionFeignException(FeignException e) {
        log.error("FeignException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<String> handleExceptionPath(MissingPathVariableException e) {
        log.error("MissingPathVariableException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleExceptionServlet(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException  is  calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> handleExceptionServlet(NonUniqueResultException e) {
        log.error("NonUniqueResultException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

}

