package online.qiqiang.forest.framework.log;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.java.util.logging.Logging;
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
@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy
public class LogPrinterAutoConfiguration {

    @Bean(ForestLogPrinterConstants.BEAN_FOREST_LOG_PROPERTIES)
    ForestLogProperties forestLogProperties() {
        return new ForestLogProperties();
    }

    /**
     * 日志打印切入点建议
     */
    @Bean(ForestLogPrinterConstants.BEAN_FOREST_POINTCUT_ADVISOR)
    @ConditionalOnProperty(name = "forest.log.enable", havingValue = "true")
    DefaultPointcutAdvisor forestPointcutAdvisor(LogMethodInterceptor logMethodInterceptor, ForestLogProperties forestLogProperties, ResourceLoader resourceLoader) {
        Logging.info(log, () -> log.info("启用日志拦截功能"));
        DefaultPointcutAdvisor forestPointcutAdvisor = new DefaultPointcutAdvisor();
        ForestMatchingPointcut forestMatchingPointcut = new ForestMatchingPointcut(forestLogProperties.getPackagePath(), resourceLoader);
        forestPointcutAdvisor.setPointcut(forestMatchingPointcut);
        forestPointcutAdvisor.setAdvice(logMethodInterceptor);
        return forestPointcutAdvisor;
    }

    /**
     * 日志打印方法拦截
     */
    @Bean(ForestLogPrinterConstants.BEAN_LOG_METHOD_INTERCEPTOR)
    @ConditionalOnProperty(name = "forest.log.enable", havingValue = "true")
    LogMethodInterceptor logMethodInterceptor(ForestLogProperties forestLogProperties, LogPrinterFunction logPrinterFunction) {
        LogMethodInterceptor advice = new LogMethodInterceptor();
        advice.setLogPrinterFunction(logPrinterFunction);
        advice.setIgnoreText(forestLogProperties.getIgnoreText());
        advice.addGlobalIgnoreReq(forestLogProperties.getIgnoreReq());
        advice.addGlobalIgnoreResp(forestLogProperties.getIgnoreResp());
        return advice;
    }

    @Bean
    @ConditionalOnMissingBean(LogPrinterFunction.class)
    public LogPrinterFunction defaultLogPrinter() {
        return new DefaultLogPrinter();
    }
}
