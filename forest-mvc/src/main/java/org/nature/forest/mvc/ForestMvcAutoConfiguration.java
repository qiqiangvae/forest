package org.nature.forest.mvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nature.forest.common.utils.LoggerUtils;
import org.nature.forest.mvc.filter.TraceFilter;
import org.nature.forest.mvc.xss.XssFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static org.nature.forest.mvc.ForestMvcConstants.*;


/**
 * forest mvc 自动配置
 *
 * @author qiqiang
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ForestMvcAutoConfiguration {

    /**
     * Forest MVC 配置类
     */
    @Bean(BEAN_FOREST_MVC_PROPERTIES)
    ForestMvcProperties forestMvcProperties() {
        return new ForestMvcProperties();
    }

    @Bean
    @ConditionalOnClass({ObjectMapper.class})
    MappingJackson2HttpMessageConverter createRestJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化时间
        objectMapper.registerModule(new JavaTimeModule());
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }

    /**
     * 注入 xss 拦截
     */
    @Bean(BEAN_FOREST_XSS_FILTER)
    @ConditionalOnProperty(name = "forest.mvc.enable-xss", havingValue = "true")
    FilterRegistrationBean<XssFilter> forestXssFilterRegistrationBean() {
        LoggerUtils.info(log, () -> log.info("开启 xss 拦截"));
        FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        xssFilterFilterRegistrationBean.setOrder(Integer.MIN_VALUE);
        xssFilterFilterRegistrationBean.setFilter(new XssFilter());
        return xssFilterFilterRegistrationBean;
    }

    /**
     * 注入 日志跟踪 拦截
     */
    @Bean(BEAN_FOREST_TRACE_FILTER)
    @ConditionalOnProperty(name = "forest.mvc.enable-trace", havingValue = "true")
    FilterRegistrationBean<TraceFilter> forestTraceFilterRegistrationBean() {
        LoggerUtils.info(log, () -> log.info("开启 日志跟踪 拦截"));
        FilterRegistrationBean<TraceFilter> traceFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        traceFilterFilterRegistrationBean.setOrder(Integer.MIN_VALUE);
        traceFilterFilterRegistrationBean.setFilter(new TraceFilter());
        return traceFilterFilterRegistrationBean;
    }

}
