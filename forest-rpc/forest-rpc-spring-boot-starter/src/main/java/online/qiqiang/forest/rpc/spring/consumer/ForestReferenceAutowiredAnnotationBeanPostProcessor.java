package online.qiqiang.forest.rpc.spring.consumer;

import online.qiqiang.forest.common.utils.reflection.AnnotationUtils;
import online.qiqiang.forest.common.utils.reflection.FieldUtils;
import online.qiqiang.forest.common.utils.reflection.PropertyUtils;
import online.qiqiang.forest.rpc.core.annotation.ForestReference;
import online.qiqiang.forest.rpc.core.consumer.client.ForestRpcConsumerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qiqiang
 */
public class ForestReferenceAutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private ForestReferenceProxyFactory factory;
    private ForestRpcConsumerProperties forestRpcClientProperties;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Set<Field> fields = FieldUtils.getAllFields(bean.getClass());
        fields = fields.stream().filter(filed -> AnnotationUtils.hasAnnotation(filed, ForestReference.class)).collect(Collectors.toSet());
        for (Field field : fields) {
            Class<?> fieldClass = field.getType();
            // 获取代理对象
            ForestReference forestReference = AnnotationUtils.getAnnotation(field, ForestReference.class);
            Object proxyObject = factory.newProxyReference(forestReference, fieldClass, forestRpcClientProperties.getTimeout());
            // 注入到 bean 中
            PropertyUtils.setValue(field, bean, proxyObject);
        }
        return bean;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        factory = beanFactory.getBean(ForestReferenceProxyFactory.class);
        forestRpcClientProperties = beanFactory.getBean(ForestRpcConsumerProperties.class);
    }
}
