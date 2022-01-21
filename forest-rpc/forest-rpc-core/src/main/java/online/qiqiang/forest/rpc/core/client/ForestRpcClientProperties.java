package online.qiqiang.forest.rpc.core.client;

import lombok.Data;

/**
 * @author qiqiang
 */
@Data
public class ForestRpcClientProperties {
    private String host;
    private int port;
    private int timeout;
}
