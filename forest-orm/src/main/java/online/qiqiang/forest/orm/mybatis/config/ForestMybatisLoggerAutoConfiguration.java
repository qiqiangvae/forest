package online.qiqiang.forest.orm.mybatis.config;

import online.qiqiang.forest.orm.mybatis.interceptor.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiqiang
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MybatisLoggerProperties.class)
public class ForestMybatisLoggerAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "forest.mybatis-log.enable", havingValue = "true")
    @ConditionalOnMissingBean(ForestMybatisLogger.class)
    public ForestMybatisLogger oceanMybatisLogger(MybatisLoggerProperties mybatisLoggerProperties,
                                                  ObjectProvider<MybatisLoggerFunction> mybatisLoggerFunctionProvider,
                                                  ObjectProvider<SlowSqlCallback> slowSqlCallbackProvider) {
        MybatisLoggerFunction mybatisLoggerFunction = mybatisLoggerFunctionProvider.getIfAvailable(() -> new DefaultMybatisLoggerFunction(mybatisLoggerProperties));
        ForestMybatisLogger oceanMybatisLogger = new ForestMybatisLogger(mybatisLoggerFunction);
        oceanMybatisLogger.setSlowSqlCallback(slowSqlCallbackProvider.getIfAvailable());
        return oceanMybatisLogger;
    }
}
