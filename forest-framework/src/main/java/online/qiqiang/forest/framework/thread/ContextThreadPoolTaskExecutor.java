package online.qiqiang.forest.framework.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Spring 上下文线程池
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class ContextThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    public ContextThreadPoolTaskExecutor() {
        setTaskDecorator(new ContextTaskDecorator());
    }
}
