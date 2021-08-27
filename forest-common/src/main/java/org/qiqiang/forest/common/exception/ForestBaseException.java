package org.qiqiang.forest.common.exception;

/**
 * 异常基础类
 *
 * @author qiqiang
 */
public abstract class ForestBaseException extends RuntimeException {
    public ForestBaseException() {
    }

    public ForestBaseException(String message) {
        super(message);
    }

    public ForestBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForestBaseException(Throwable cause) {
        super(cause);
    }

    public ForestBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
