package online.qiqiang.forest.orm.sharding;

import online.qiqiang.forest.orm.exception.ForestOrmException;

/**
 * @author qiqiang
 */
public class ForestRefreshShardingException extends ForestOrmException {
    public ForestRefreshShardingException(String message) {
        super(message);
    }

    public ForestRefreshShardingException(Throwable cause) {
        super(cause);
    }
}
