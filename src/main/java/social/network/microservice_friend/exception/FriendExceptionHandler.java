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
        log.error("BusinessLogicException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Message> handleExceptionNull(NullPointerException e) {
        log.error("NullPointerException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Message> handleExceptionIllegalA(IllegalArgumentException e) {
        log.error("IllegalArgumentException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Message> handleException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());


    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Message> handleExceptionFeignException(FeignException e) {
        log.error("FeignException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());

    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Message> handleExceptionPath(MissingPathVariableException e) {
        log.error("MissingPathVariableException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Message> handleExceptionServlet(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException  is  calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());

    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<Message> handleExceptionServlet(NonUniqueResultException e) {
        log.error("NonUniqueResultException  is    calling {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Message.builder()
                        .message(e.getMessage())
                        .build());

    }

}

