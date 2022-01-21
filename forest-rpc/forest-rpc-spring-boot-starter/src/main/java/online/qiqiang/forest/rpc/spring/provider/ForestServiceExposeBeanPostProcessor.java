package online.qiqiang.forest.rpc.spring.provider;

import online.qiqiang.forest.rpc.core.provider.ServiceRegister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author qiqiang
 */
public class ForestServiceExposeBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ServiceRegister.register(bean);
        return bean;
    }
}
