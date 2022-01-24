package online.qiqiang.forest.rpc.spring.consumer;

import online.qiqiang.forest.rpc.core.consumer.client.ChannelWriter;
import online.qiqiang.forest.rpc.core.consumer.client.ForestRpcClient;
import online.qiqiang.forest.rpc.core.consumer.client.ForestRpcConsumerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author qiqiang
 */
public class ForestRpcClientAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "forest.rpc.consumer")
    ForestRpcConsumerProperties forestRpcClientProperties() {
        return new ForestRpcConsumerProperties();
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    ForestRpcClient forestRpcClient(ForestRpcConsumerProperties forestRpcClientProperties) {
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
