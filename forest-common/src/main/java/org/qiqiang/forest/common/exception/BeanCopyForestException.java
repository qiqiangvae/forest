package org.qiqiang.forest.common.exception;

/**
 * bean copy 异常
 *
 * @author qiqiang
 */
public class BeanCopyForestException extends ForestBaseException {
    public BeanCopyForestException(String message) {
        super(message);
    }

    public BeanCopyForestException(Throwable cause) {
        super(cause);
    }

    public BeanCopyForestException(String message, Throwable cause) {
        super(message, cause);
    }
}
