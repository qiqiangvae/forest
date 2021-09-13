package org.qiqiang.forest.framework.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author qiqiang
 */
@Slf4j
@SuppressWarnings("unused")
public class ZookeeperRemoteContext implements RemoteContext, InitializingBean, ApplicationContextAware {


    @Override
    public void afterPropertiesSet() {
        log.info("启用 Zookeeper 远程上下文");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        throwException();
    }

    private void throwException() {
        throw new ForestContextException("zookeeper remote context not support yet");
    }

    @Override
    public String get(String key) {
        throwException();
        return null;
    }

    @Override
    public void set(String key, String value) {
        throwException();
    }

    @Override
    public void clear() {
        throwException();
    }
}
