package online.qiqiang.forest.rpc.core.provider.server;

import lombok.Data;

/**
 * @author qiqiang
 */
@Data
public class ForestRpcProviderProperties {
    /**
     * 注册地址
     */
    private String registerUrl;
    /**
     * 服务提供端口
     */
    private int exposePort;
    /**
     * netty worker 配置
     */
    private int workers;
}
