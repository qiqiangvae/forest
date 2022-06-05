package online.qiqiang.forest.framework.thread;

import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 带有分组管理功能的线程池
 * 使用场景：
 * 当业务需要使用多线程来处理任务时，需要等待所有任务都执行结束才往后执行，此线程池提供 await 方法统一等待。
 * 当某个任务发生异常时，需要取消所有的任务，其它线程池无法提供此功能。
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class GroupThreadPoolExecutor extends ContextThreadPoolTaskExecutor {
    private final Map<String, ThreadGroup> groupMap = new ConcurrentHashMap<>();


    /**
     * 创建线程组
     *
     * @param group         组名
     * @param aliveSecond   存活时间，单位秒，超过该时间会自动取消所有任务
     * @param shutdownGroup 如果异常是否取消改组内所有的任务
     * @return 线程组
     */
    public ThreadGroup createGroup(String group, long aliveSecond, boolean shutdownGroup) {
        return createGroup(group, aliveSecond, shutdownGroup, 100);
    }

    /**
     * 创建线程组
     *
     * @param group         组名
     * @param aliveSecond   存活时间，单位秒，超过该时间会自动取消所有任务
     * @param shutdownGroup 如果异常是否取消改组内所有的任务
     * @param masSize       任务组最大任务数，如果超过超过最大数量会阻塞
     * @return 线程组
     */
    public ThreadGroup createGroup(String group, long aliveSecond, boolean shutdownGroup, int masSize) {
        return groupMap.computeIfAbsent(group, s -> new ThreadGroup(group, this, aliveSecond, shutdownGroup, masSize));
    }

    /**
     * 提交组任务
     *
     * @param group 组名
     * @param task  任务
     * @return Future
     */
    public Future<?> submit(String group, Runnable task) {
        AtomicReference<Future<?>> future = new AtomicReference<>();
        // 加到组缓存中
        groupMap.compute(group, (name, instance) -> {
            nonNull(name, instance);
            // 提交任务后加入组中
            GroupRunnable groupRunnable = new GroupRunnable(instance, task);
            try {
                future.set(super.submit(groupRunnable));
            } catch (Exception e) {
                // 如果提交任务出现异常了，且任务已经被初始化了，那么需要从队列中移除任务
                if (groupRunnable.isInit()) {
                    instance.pollTask();
                }
                // 取消所有任务
                if (instance.isShutdownIfError()) {
                    instance.cancel(ThreadGroup.Status.ERROR_CANCEL);
                }
                throw e;
            }
            instance.addFuture(future.get());
            return instance;
        });
        return future.get();
    }

    /**
     * 取消该组的全部任务
     *
     * @param group 组名
     */
    public void cancel(String group) {
        ThreadGroup instance = groupMap.get(group);
        nonNull(group, instance);
        // 正常取消
        instance.cancel(ThreadGroup.Status.NORMAL_CANCEL);
    }

    /**
     * 移除引用，防止内存泄漏
     * 注意，这里仅仅只是移除引用，不能终止任务
     *
     * @param group 组名
     */
    public void removeGroup(String group) {
        groupMap.remove(group);
    }


    /**
     * 等待组内所有线程执行结束
     *
     * @param group 组名
     */
    @SneakyThrows
    public void await(String group) {
        ThreadGroup instance = groupMap.get(group);
        nonNull(group, instance);
        instance.await();
    }

    private void nonNull(String group, ThreadGroup instance) {
        if (instance == null) {
            throw new NoSuchThreadGroupException(group);
        }
    }
}
