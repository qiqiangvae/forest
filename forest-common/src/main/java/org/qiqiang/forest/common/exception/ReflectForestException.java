package org.qiqiang.forest.common.exception;

/**
 * @author qiqiang
 */
public class ReflectForestException extends ForestBaseException{
    public ReflectForestException() {
    }

    public ReflectForestException(String message) {
        super(message);
    }

    public ReflectForestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectForestException(Throwable cause) {
        super(cause);
    }

    public ReflectForestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
