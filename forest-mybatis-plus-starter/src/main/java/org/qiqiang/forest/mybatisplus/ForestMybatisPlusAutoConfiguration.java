package org.qiqiang.forest.mybatisplus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * forest mybatis plus 自动配置类
 *
 * @author qiqiang
 */
@Configuration
public class ForestMybatisPlusAutoConfiguration {

    @Bean(name = ForestMybatisPlusConst.BEAN_FOREST_SQL_INJECTOR)
    public ForestSqlInjector forestSqlInjector() {
        return new ForestSqlInjector();
    }
}
