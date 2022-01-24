package online.qiqiang.forest.rpc.core.consumer.client;

import lombok.Data;

/**
 * @author qiqiang
 */
@Data
public class ForestRpcConsumerProperties {
    /**
     * 注册中心地址
     */
    private String url;
    /**
     * 调用超时时间
     */
    private int timeout;

    public void setUrl(String url) {
        if (!url.startsWith("forest")) {
            url = "forest://" + url;
        }
        this.url = url;
    }
}
