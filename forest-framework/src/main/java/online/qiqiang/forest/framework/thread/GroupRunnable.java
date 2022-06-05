package online.qiqiang.forest.framework.thread;

/**
 * 可传递上下文的 Runnable
 *
 * @author qiqiang
 */
public class GroupRunnable extends ContextRunnable {

    private final ThreadGroup group;
    private volatile boolean init;

    public GroupRunnable(ThreadGroup group, Runnable runnable) {
        super(runnable);
        this.group = group;
        init();
    }

    private void init() {
        // 添加一个任务到任务组，因为是阻塞的，可以保证任务组的最大任务数量
        this.group.offerTask();
        this.init = true;

    }

    public boolean isInit() {
        return init;
    }

    @Override
    public void run() {
        try {
            super.run();
            group.incrDone();
        } catch (Exception e) {
            // 让任务组立刻感知到异常发生了
            group.setCause(e);
        } finally {
            // 任务结束，移除一个任务，留下看空间给下一个任务
            this.group.pollTask();
        }
    }
}
