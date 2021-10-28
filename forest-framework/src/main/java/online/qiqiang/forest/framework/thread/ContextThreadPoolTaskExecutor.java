package online.qiqiang.forest.framework.thread;

/**
 * Spring 上下文线程池
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class ContextThreadPoolTaskExecutor extends MonitorThreadPoolTaskExecutor {
    public ContextThreadPoolTaskExecutor() {
        setTaskDecorator(new ContextTaskDecorator());
    }
}
