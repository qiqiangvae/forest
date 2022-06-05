package online.qiqiang.forest.framework.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.java.util.function.NothingConsumer;
import online.qiqiang.forest.common.java.util.logging.Logging;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiqiang
 */
@Slf4j
public class ThreadGroup {

    private final Object awaitLock = new Object();
    private final BlockingQueue<Object> queue;

    private static final Object QUEUE_OBJECT = new Object();

    /**
     * 任务组名称
     */
    private final String name;
    private final GroupThreadPoolExecutor parent;

    private final List<Future<?>> futures;
    /**
     * 完成任务数量
     */
    private final AtomicInteger doneCount;

    /**
     * 超时时间节点
     */
    private final long shutdownPoint;
    /**
     * 如果异常是否取消改组内所有的任务
     */
    private final boolean shutdownIfError;
    private final Disposable disposable;

    /**
     * 0 正常状态，1 正常取消 ,2 异常取消，3 完成
     */
    private volatile Status status = Status.NORMAL;

    /**
     * 异常
     */
    private Throwable cause;

    ThreadGroup(String name, GroupThreadPoolExecutor parent, long alive, boolean shutdownIfError, int maxSize) {
        this.name = name;
        this.parent = parent;
        this.futures = new ArrayList<>();
        this.shutdownPoint = System.currentTimeMillis() + alive * 1000;
        this.shutdownIfError = shutdownIfError;
        this.doneCount = new AtomicInteger(0);
        this.queue = new ArrayBlockingQueue<>(maxSize);
        // 创建一个定时器，定时清除过期的任务
        disposable = Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
                .subscribe(l -> {
                    if (shutdownPoint <= System.currentTimeMillis()) {
                        // 如果超过了任务中断时间点，那么立即取消任务
                        cancel(Status.TIMEOUT_CANCEL);
                        cause = new TimeoutException("任务组因超过最长存活时间而被取消.");
                    }
                });
    }

    void addFuture(Future<?> future) {
        canDoThatWhenNormalStatus(() -> futures.add(future));
    }

    /**
     * 添加一个任务，阻塞
     */
    void offerTask() {
        canDoThatWhenNormalStatus(() -> {
            queue.put(QUEUE_OBJECT);
            Logging.debug(log, () -> log.debug("成功添加任务，[{}]任务组当前任务数[{}]", name, queue.size()));
        });
    }

    /**
     * 任务完成，取出一个任务，阻塞的
     */
    @SneakyThrows
    void pollTask() {
        queue.take();
        Logging.debug(log, () -> log.debug("任务结束，[{}]任务组当前任务数[{}]", name, queue.size()));
    }

    @SneakyThrows
    void canDoThatWhenNormalStatus(NothingConsumer consumer) {
        if (status == Status.NORMAL) {
            consumer.accept();
        } else {
            throw new IllegalStateException("当前线程组状态为" + status + "，无法添加任务");
        }
    }

    /**
     * 设置异常，并取消线程组任务
     *
     * @param cause 异常
     */
    void setCause(Throwable cause) {
        if (cause == null) {
            log.warn("异常信息为空.");
        } else if (shutdownIfError) {
            this.cause = cause;
            // 因某个任务发生异常而取消
            cancel(Status.ERROR_CANCEL);
        }
    }

    void incrDone() {
        doneCount.incrementAndGet();
    }

    /**
     * 取消该组的全部任务
     */
    @SneakyThrows
    void cancel(Status s) {
        log.info("取消[{}]组的所有任务", name);
        synchronized (this) {
            // 只有是正常状态并且需要更新为取消状态才能取消
            if (this.status == Status.NORMAL && Status.isCancelStatus(s)) {
                end();
                this.status = s;
                for (Future<?> future : futures) {
                    // 任务没有完成或者没有取消的时候，强制取消该任务
                    if (!future.isDone() && !future.isCancelled()) {
                        future.cancel(true);
                    }
                }
                log.info(this.status.endMessage(name));
            }
        }
    }

    private synchronized void end() {
        parent.removeGroup(name);
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    boolean isShutdownIfError() {
        return shutdownIfError;
    }

    @SneakyThrows
    protected void await() {
        log.info("等待[{}]组的所有任务完成", name);
        synchronized (awaitLock) {
            if (status == Status.NORMAL) {
                try {
                    for (Future<?> future : futures) {
                        future.get();
                    }
                    // 正常结束
                    status = Status.COMPLETED;
                } catch (Exception e) {
                    // 任务被取消了
                    if (e instanceof CancellationException && Status.isCancelStatus(status)) {
                        Logging.debug(log, () -> log.debug("任务被取消了."));
                        // 异常取消的需要抛出异常
                        if (status == Status.ERROR_CANCEL) {
                            if (cause == null) {
                                cause = e;
                            }
                        }
                    } else {
                        // 立即终止 group
                        if (shutdownIfError) {
                            log.info("[{}]存在任务异常，取消该组所有的任务", name);
                            this.cancel(Status.ERROR_CANCEL);
                        }
                        if (e instanceof ExecutionException) {
                            cause = e.getCause();
                        } else {
                            cause = e;
                        }
                    }
                }
                end();
                log.info(status.endMessage(name));
                log.info("[{}]任务组正常完成比例[{}/{}]", name, doneCount, futures.size());
                if (cause != null && this.status != Status.COMPLETED) {
                    throw cause;
                }
            }
        }
    }

    enum Status {
        /**
         * 正常
         */
        NORMAL(0) {
            @Override
            public String endMessage(String groupName) {
                return "任务[" + groupName + "]还未结束";
            }
        },
        /**
         * 正常取消
         */
        NORMAL_CANCEL(1) {
            @Override
            public String endMessage(String groupName) {
                return "任务[" + groupName + "]因被正常取消而结束";
            }
        },
        /**
         * 异常取消
         */
        ERROR_CANCEL(2) {
            @Override
            public String endMessage(String groupName) {
                return "任务[" + groupName + "]因异常取消而结束";
            }
        },

        /**
         * 超时取消
         */
        TIMEOUT_CANCEL(3) {
            @Override
            public String endMessage(String groupName) {
                return "任务[" + groupName + "]因超时取消而结束";
            }
        },
        /**
         * 完成
         */
        COMPLETED(4) {
            @Override
            public String endMessage(String groupName) {
                return "任务[" + groupName + "]因全部完成而结束";
            }
        };

        final int code;

        Status(int code) {
            this.code = code;
        }

        public String endMessage(String groupName) {
            throw new AbstractMethodError();
        }

        static boolean isCancelStatus(Status s) {
            return s == NORMAL_CANCEL || s == ERROR_CANCEL || s == TIMEOUT_CANCEL;
        }
    }
}
