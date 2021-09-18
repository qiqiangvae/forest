package org.nature.forest.framework.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author qiqiang
 */
@Slf4j
public class SimpleRemoteContext implements RemoteContext, InitializingBean {


    @Override
    public void afterPropertiesSet() {
        log.info("启用默认远程上下文");
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

    private void throwException() {
        throw new ForestContextException("simple 不支持远程上下文，请使用 redis");
    }
}
