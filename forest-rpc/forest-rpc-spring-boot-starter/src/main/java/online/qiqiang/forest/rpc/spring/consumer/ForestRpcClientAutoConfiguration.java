package online.qiqiang.forest.rpc.spring.consumer;

import online.qiqiang.forest.rpc.core.client.ChannelWriter;
import online.qiqiang.forest.rpc.core.client.ForestRpcClient;
import online.qiqiang.forest.rpc.core.client.ForestRpcClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author qiqiang
 */
public class ForestRpcClientAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "forest.rpc.consumer")
    ForestRpcClientProperties forestRpcClientProperties() {
        return new ForestRpcClientProperties();
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    ForestRpcClient forestRpcClient(ForestRpcClientProperties forestRpcClientProperties) {
        return new ForestRpcClient(forestRpcClientProperties);
    }

    @Bean
    ChannelWriter channelWriter(ForestRpcClient forestRpcClient) {
        return forestRpcClient.getChannelWriter();
    }

    @Bean
    ForestReferenceProxyFactory forestReferenceProxyFactory(ChannelWriter channelWriter) {
        return new ForestReferenceProxyFactory(channelWriter);
    }
}
