package org.nature.forest.framework.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.nature.forest.common.utils.JsonUtils;
import org.nature.forest.common.java.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * 用项目自带的 JsonUtils 实现
 *
 * @author qiqiang
 */
@Slf4j
public class DefaultLogPrinter implements LogPrinterFunction, InitializingBean, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public Map<String, Object> convert2Map(Object object) {
        return JsonUtils.convert2Map(object);
    }

    @Override
    public String toString(Map<String, Object> jsonMap) {
        return JsonUtils.write2String(jsonMap);
    }

    @Override
    public void afterPropertiesSet() {
        // 加载自定义模块
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        try {
            JacksonLogPrinterCustomizer customizer = beanFactory.getBean(JacksonLogPrinterCustomizer.class);
            customizer.getModules().forEach(objectMapper::registerModule);
        } catch (BeansException exception) {
            Logging.warn(log, () -> log.warn("没有找到 JacksonLogPrinterCustomizer"));
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
