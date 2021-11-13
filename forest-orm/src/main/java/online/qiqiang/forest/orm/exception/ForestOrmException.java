package online.qiqiang.forest.orm.exception;

import online.qiqiang.forest.common.exception.BaseForestException;

/**
 * @author qiqiang
 */
public class ForestOrmException extends BaseForestException {
    public ForestOrmException(String message) {
        super(message);
    }

    public ForestOrmException(Throwable cause) {
        super(cause);
    }
}
