package org.qiqiang.forest.mvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author qiqiang
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ObjectMapper.class})
@ConditionalOnBean({ObjectMapper.class})
public class ForestMvcAutoConfiguration {

    /**
     * 注入 spring 内置的 ObjectMapper
     */
    private final ObjectMapper objectMapper;

    @Bean
    MappingJackson2HttpMessageConverter createRestJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化时间
        objectMapper.registerModule(new JavaTimeModule());
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }
}
