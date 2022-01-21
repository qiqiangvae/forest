package online.qiqiang.forest.rpc.core.server;

import lombok.Data;

/**
 * @author qiqiang
 */
@Data
public class ForestRpcServerProperties {
    private int port;
    private int workers;
}
