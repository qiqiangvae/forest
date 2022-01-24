package online.qiqiang.forest.rpc.spring.provider;

import online.qiqiang.forest.rpc.core.provider.server.ForestRpcProviderProperties;
import online.qiqiang.forest.rpc.core.provider.server.ForestRpcServer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author qiqiang
 */
public class ForestRpcServerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "forest.rpc.provider")
    public ForestRpcProviderProperties forestRpcServerProperties() {
        return new ForestRpcProviderProperties();
    }

    @Bean
    public ForestServiceExposeBeanPostProcessor forestServiceExposeRegistryPostProcessor() {
        return new ForestServiceExposeBeanPostProcessor();
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public ForestRpcServer forestRpcServer(ForestRpcProviderProperties forestRpcServerProperties) {
        return new ForestRpcServer(forestRpcServerProperties);
    }
}
