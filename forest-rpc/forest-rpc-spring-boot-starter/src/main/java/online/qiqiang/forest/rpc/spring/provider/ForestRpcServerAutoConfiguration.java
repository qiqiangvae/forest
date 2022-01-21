package online.qiqiang.forest.rpc.spring.provider;

import online.qiqiang.forest.rpc.core.server.ForestRpcServer;
import online.qiqiang.forest.rpc.core.server.ForestRpcServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author qiqiang
 */
public class ForestRpcServerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "forest.rpc.provider")
    public ForestRpcServerProperties forestRpcServerProperties() {
        return new ForestRpcServerProperties();
    }

    @Bean
    public ForestServiceExposeBeanPostProcessor forestServiceExposeRegistryPostProcessor() {
        return new ForestServiceExposeBeanPostProcessor();
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public ForestRpcServer forestRpcServer(ForestRpcServerProperties forestRpcServerProperties) {
        return new ForestRpcServer(forestRpcServerProperties);
    }
}
