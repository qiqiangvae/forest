package org.qiqiang.forest.common.exception;

/**
 * 异常基础类
 *
 * @author qiqiang
 */
public abstract class BaseForestException extends RuntimeException {
    @SuppressWarnings("unused")
    public BaseForestException() {
    }

    @SuppressWarnings("unused")
    public BaseForestException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public BaseForestException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unused")
    public BaseForestException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public BaseForestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
