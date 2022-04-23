package site.heaven96.limiter.exception;

/**
 * 超过限制例外
 *
 * @author lrx
 * @date 2022/04/23
 */
public class OverLimitException extends RuntimeException {
    public OverLimitException() {
    }

    public OverLimitException(String message) {
        super(message);
    }

    public OverLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverLimitException(Throwable cause) {
        super(cause);
    }

    public OverLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
