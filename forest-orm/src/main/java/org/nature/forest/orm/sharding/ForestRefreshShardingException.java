package org.nature.forest.orm.sharding;

import org.nature.forest.common.exception.BaseForestException;

/**
 * @author qiqiang
 */
public class ForestRefreshShardingException extends BaseForestException {
    public ForestRefreshShardingException(String message) {
        super(message);
    }

    public ForestRefreshShardingException(Throwable cause) {
        super(cause);
    }
}
