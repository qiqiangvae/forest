package org.nature.forest.framework.context;

import lombok.extern.slf4j.Slf4j;
import org.nature.forest.common.java.util.logging.Logging;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author qiqiang
 */
@Slf4j
public class SimpleRemoteContext implements RemoteContext, InitializingBean {


    @Override
    public void afterPropertiesSet() {
        Logging.info(log, () -> log.info("请使用 Redis 远程上下文."));
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void set(String key, String value) {
    }

    @Override
    public void clear() {
    }

}
