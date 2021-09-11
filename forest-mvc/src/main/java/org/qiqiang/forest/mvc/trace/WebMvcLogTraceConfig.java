package org.qiqiang.forest.mvc.trace;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : Zhang Huang
 * @date : 2021-09-10 3:03 下午
 */
@ConditionalOnProperty(name = "forest.mvc.enable-log-trace", havingValue = "true")
public class WebMvcLogTraceConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TraceInterceptor())
                .addPathPatterns("/**")
                .order(0);
    }
}
