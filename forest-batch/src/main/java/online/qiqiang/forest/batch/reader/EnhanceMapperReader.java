package online.qiqiang.forest.batch.reader;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.java.util.logging.Logging;
import online.qiqiang.forest.orm.mybatisplus.enhance.ForestEnhanceMapper;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * mybatis plus 增强版的 mapper reader
 *
 * @author qiqiang
 */
@Slf4j
@SuppressWarnings("unused")
public class EnhanceMapperReader<T> extends ItemStreamSupport implements ItemReader<T> {
    private static final int count = 100;

    private final Wrapper<T> wrapper;
    private final ForestEnhanceMapper<T> enhanceMapper;

    private final T EMPTY;
    private final AtomicLong total = new AtomicLong(0);
    private final AtomicLong totalConsume = new AtomicLong(0);
    private final BlockingQueue<T> queue = new ArrayBlockingQueue<>(count);

    private CompletableFuture<Void> completableFuture;
    private volatile boolean stop = false;

    public EnhanceMapperReader(ForestEnhanceMapper<T> enhanceMapper, Wrapper<T> wrapper, Class<T> modelClass) {
        this.wrapper = wrapper;
        this.enhanceMapper = enhanceMapper;
        try {
            EMPTY = modelClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T read() {
        if (completableFuture == null) {
            initThread();
        }
        T consume = consume();
        // 确实是没数据了，结束吧，这是唯一条件
        if (consume == EMPTY) {
            return null;
        }
        totalConsume.incrementAndGet();
        return consume;
    }

    @Override
    public void open(@NonNull ExecutionContext executionContext) {
        initThread();
    }

    private T consume() {
        // 如果停止获取数据了，并且队列也没有数据了，这时候才是真正的停止
        if (stop && queue.isEmpty()) {
            Logging.info(log, () -> log.info("没有数据了，共消费[{}]条数据", totalConsume.get()));
            return EMPTY;
        }
        T item;
        try {
            item = queue.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // 如果超时未获取到，说明此时队列里没有数据，可能是 fetch 没有 consume 快，那么再尝试一次
            item = consume();
        }
        if (item == null) {
            item = consume();
        }
        return item;
    }

    private synchronized void initThread() {
        if (null == enhanceMapper) {
            stop = true;
            queue.clear();
            return;
        }
        completableFuture = CompletableFuture.runAsync(() -> {
            enhanceMapper.fetchByStream(wrapper, new ResultHandler<T>() {
                @SneakyThrows
                @Override
                public void handleResult(ResultContext<? extends T> resultContext) {
                    queue.put(resultContext.getResultObject());
                    total.incrementAndGet();
                }
            });
            Logging.info(log, () -> log.info("没有数据了，共获取到[{}]条数据", total.get()));
            stop = true;
        });
    }

    @Override
    public void close() {
        completableFuture.complete(null);
        completableFuture.cancel(true);
    }
}
