package social.network.microservice_friend.exception;

import feign.FeignException;
import org.hibernate.NonUniqueResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FriendExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<String> handleException(BusinessLogicException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleExceptionNull() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Что-то пошло не так, задайте параметры поиска");

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleExceptionIllegalA(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleExceptionFeignException(FeignException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<String> handleExceptionPath(MissingPathVariableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleExceptionServlet(MissingServletRequestParameterException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> handleExceptionServlet(NonUniqueResultException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

}

