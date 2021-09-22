package org.nature.forest.common.exception;

/**
 * bean copy 异常
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class BeanCopyForestException extends BaseForestException {
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
