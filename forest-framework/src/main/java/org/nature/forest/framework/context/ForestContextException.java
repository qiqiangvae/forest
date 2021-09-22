package org.nature.forest.framework.context;

import org.nature.forest.common.exception.BaseForestException;

/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class ForestContextException extends BaseForestException {
    public ForestContextException() {
    }

    public ForestContextException(String message) {
        super(message);
    }

    public ForestContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForestContextException(Throwable cause) {
        super(cause);
    }

    public ForestContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
