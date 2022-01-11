package online.qiqiang.forest.framework.context;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiqiang
 */
@Configuration(proxyBeanMethods = false)
public class ForestContextAutoConfiguration {

    @Bean
    ForestContextProperties forestContextProperties() {
        return new ForestContextProperties();
    }


    @Bean
    @ConditionalOnProperty(name = "forest.context.type", havingValue = "simple")
    RemoteContext simpleRemoteContext() {
        return new SimpleRemoteContext();
    }

    @Bean
    @ConditionalOnProperty(name = "forest.context.type", havingValue = "redis")
    RemoteContext redisRemoteContext() {
        return new RedisRemoteContext();
    }

    @Bean
    @ConditionalOnProperty(name = "forest.context.type", havingValue = "zookeeper")
    RemoteContext zookeeperRemoteContext() {
        return new ZookeeperRemoteContext();
    }

    @Bean
    ForestContext forestContext(ObjectProvider<RemoteContext> remoteContextProvider) {
        ForestContext forestContext = new ForestContext();
        ForestContext.setRemoteContext(remoteContextProvider.getIfAvailable(SimpleRemoteContext::new));
        return forestContext;
    }

}
