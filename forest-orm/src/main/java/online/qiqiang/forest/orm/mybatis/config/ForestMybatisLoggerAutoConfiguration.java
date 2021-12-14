package online.qiqiang.forest.orm.mybatis.config;

import online.qiqiang.forest.orm.mybatis.interceptor.DefaultSlowSqlCallback;
import online.qiqiang.forest.orm.mybatis.interceptor.MybatisLoggerProperties;
import online.qiqiang.forest.orm.mybatis.interceptor.ForestMybatisLogger;
import online.qiqiang.forest.orm.mybatis.interceptor.SlowSqlCallback;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiqiang
 */
@Configuration(proxyBeanMethods = false)
public class ForestMybatisLoggerAutoConfiguration {


    @Bean
    public MybatisLoggerProperties mybatisLoggerProperties() {
        return new MybatisLoggerProperties();
    }

    @Bean
    @ConditionalOnMissingBean(SlowSqlCallback.class)
    public DefaultSlowSqlCallback defaultSlowSqlCallback() {
        return new DefaultSlowSqlCallback();
    }

    @Bean
    @ConditionalOnMissingBean(ForestMybatisLogger.class)
    public ForestMybatisLogger oceanMybatisLogger(MybatisLoggerProperties mybatisLoggerProperties, SlowSqlCallback slowSqlCallback) {
        ForestMybatisLogger oceanMybatisLogger = new ForestMybatisLogger(mybatisLoggerProperties);
        oceanMybatisLogger.setSlowSqlCallback(slowSqlCallback);
        return oceanMybatisLogger;
    }
}
