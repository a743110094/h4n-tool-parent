package site.heaven96.test;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.heaven96.limiter.exception.OverLimitException;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(OverLimitException.class)
    public String sqlExpHandler(OverLimitException e) {
        return e.getMessage();
    }
}
