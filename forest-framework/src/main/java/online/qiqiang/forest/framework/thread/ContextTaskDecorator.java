package online.qiqiang.forest.framework.thread;

import org.springframework.core.task.TaskDecorator;

/**
 * 上下文任务探测器，用于实现 Spring 任务线程池传递上下文
 *
 * @author qiqiang
 */
public class ContextTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        return new ContextRunnable(runnable);
    }
}
