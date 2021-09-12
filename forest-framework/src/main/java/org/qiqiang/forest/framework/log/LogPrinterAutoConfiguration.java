package org.qiqiang.forest.framework.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ResourceLoader;

/**
 * @author qiqiang
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class LogPrinterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "forestLogProperties")
    ForestLogProperties forestLogProperties() {
        return new ForestLogProperties();
    }

    /**
     * 注入方法参数打印拦截器
     */
    @Bean
    @ConditionalOnMissingBean(name = "forestPointcutAdvisor")
    @ConditionalOnProperty(name = "forest.log.enable", havingValue = "true")
    DefaultPointcutAdvisor forestPointcutAdvisor(ObjectMapper objectMapper, ForestLogProperties forestLogProperties, ResourceLoader resourceLoader) {
        log.info("启用日志拦截功能");
        DefaultPointcutAdvisor forestPointcutAdvisor = new DefaultPointcutAdvisor();
        ForestMatchingPointcut forestMatchingPointcut = new ForestMatchingPointcut(forestLogProperties.getPackagePath(), resourceLoader);
        forestPointcutAdvisor.setPointcut(forestMatchingPointcut);
        Advice advice = getAdvice(objectMapper, forestLogProperties);
        forestPointcutAdvisor.setAdvice(advice);
        return forestPointcutAdvisor;
    }

    private Advice getAdvice(ObjectMapper objectMapper, ForestLogProperties forestMvcProperties) {
        LogMethodInterceptor advice = new LogMethodInterceptor();
        advice.setObjectMapper(objectMapper);
        advice.setIgnoreText(forestMvcProperties.getIgnoreText());
        advice.addGlobalIgnoreReq(forestMvcProperties.getIgnoreReq());
        advice.addGlobalIgnoreResp(forestMvcProperties.getIgnoreResp());
        return advice;
    }

}
