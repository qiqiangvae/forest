package org.nature.forest.framework.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @ConditionalOnMissingBean(RemoteContext.class)
    RemoteContext simpleRemoteContext() {
        return new SimpleRemoteContext();
    }

    @Bean
    ForestContext forestContext(RemoteContext remoteContext) {
        ForestContext forestContext = new ForestContext();
        ForestContext.setRemoteContext(remoteContext);
        return forestContext;
    }

}
