package online.qiqiang.forest.common.exception;

/**
 * 异常基础类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public abstract class BaseForestException extends RuntimeException {
    public BaseForestException() {
    }

    public BaseForestException(String message) {
        super(message);
    }

    public BaseForestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseForestException(Throwable cause) {
        super(cause);
    }

    public BaseForestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
