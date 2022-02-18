package online.qiqiang.forest.rpc.core.consumer.client;

import lombok.Data;

/**
 * @author qiqiang
 */
@Data
public class ForestRpcConsumerProperties {
    /**
     * 注册地址
     */
    private String registerUrl;
    /**
     * 注册中心地址
     */
    private String connectUrl;
    /**
     * 调用超时时间
     */
    private int timeout;

    public void setConnectUrl(String connectUrl) {
        if (!connectUrl.startsWith("forest")) {
            connectUrl = "forest://" + connectUrl;
        }
        this.connectUrl = connectUrl;
    }
}
