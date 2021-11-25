package online.qiqiang.forest.security;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.security.intercept.BasicInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author qiqiang
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy
public class ForestSecurityAutoConfiguration {

    @Bean
    public BasicInterceptor basicInterceptor() {
        return new BasicInterceptor();
    }
}
