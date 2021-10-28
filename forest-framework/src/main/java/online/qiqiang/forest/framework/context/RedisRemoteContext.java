package online.qiqiang.forest.framework.context;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.java.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author qiqiang
 */
@Slf4j
public class RedisRemoteContext implements RemoteContext, InitializingBean, ApplicationContextAware {
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void afterPropertiesSet() {
        Logging.info(log, () -> log.info("启用 Redis 远程上下文"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

    @Override
    public String get(String key) {
        return (String) stringRedisTemplate.opsForHash().get(ForestContext.getRemoteSequence(), key);
    }

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForHash().put(ForestContext.getRemoteSequence(), key, value);
    }

    @Override
    public void clear() {
        stringRedisTemplate.delete(ForestContext.getRemoteSequence());
    }
}
