package online.qiqiang.forest.security;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.security.callback.AuthenticateCallback;
import online.qiqiang.forest.security.aspect.BasicAspect;
import online.qiqiang.forest.security.support.UserSupport;
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
    public BasicAspect basicInterceptor(AuthenticateCallback authenticateCallback, UserSupport userSupport) {
        BasicAspect basicInterceptor = new BasicAspect();
        basicInterceptor.setAuthenticateCallback(authenticateCallback);
        basicInterceptor.setUserSupport(userSupport);
        return basicInterceptor;
    }
}
