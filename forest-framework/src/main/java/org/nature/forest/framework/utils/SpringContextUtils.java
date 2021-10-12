package org.nature.forest.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.nature.forest.common.utils.Logging;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author qiqiang
 */
@SuppressWarnings({"unused","unchecked"})
@Component
@Slf4j
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.context = applicationContext;
        Logging.debug(log, () -> log.debug("SpringContextUtils 初始化成功."));
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }


    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    public static <T extends Annotation> Map<String, Object> getBeansWithAnnotation(Class<T> clazz) {
        return context.getBeansWithAnnotation(clazz);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
