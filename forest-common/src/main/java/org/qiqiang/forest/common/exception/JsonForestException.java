package org.qiqiang.forest.common.exception;

/**
 * json 异常
 *
 * @author qiqiang
 */
public class JsonForestException extends BaseForestException {
    public JsonForestException(Throwable cause) {
        super(cause);
    }

    public JsonForestException(String message, Throwable cause) {
        super(message, cause);
    }
}
