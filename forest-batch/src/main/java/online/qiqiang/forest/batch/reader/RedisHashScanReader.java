package online.qiqiang.forest.batch.reader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qiqiang
 */
@Slf4j
@SuppressWarnings("unused")
public class RedisHashScanReader extends ItemStreamSupport implements ItemReader<Map.Entry<String, String>> {
    private static final int count = 3000;
    private final StringRedisTemplate stringRedisTemplate;
    private final String redisKey;
    private ScanOptions scanOptions;
    private final BlockingQueue<Map.Entry<String, String>> queue = new ArrayBlockingQueue<>(count);
    private final Map.Entry<String, String> EMPTY = new AbstractMap.SimpleEntry<>("EMPTY", "EMPTY");
    /**
     * fetch Thread 是否停止
     */
    private volatile boolean stop = false;
    private CompletableFuture<Void> completableFuture;
    private final AtomicLong total = new AtomicLong(0);
    private final AtomicLong totalConsume = new AtomicLong(0);

    public RedisHashScanReader(StringRedisTemplate stringRedisTemplate, String redisKey) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisKey = redisKey;
    }

    @Override
    public Map.Entry<String, String> read() {
        if (completableFuture == null) {
            initThread();
        }
        Map.Entry<String, String> consume = consume();
        // 确实是没数据了，结束吧，这是唯一条件
        if (consume == EMPTY) {
            return null;
        }
        totalConsume.incrementAndGet();
        return consume;
    }

    private Map.Entry<String, String> consume() {
        // 如果停止获取数据了，并且队列也没有数据了，这时候才是真正的停止
        if (stop && queue.isEmpty()) {
            log.info("[{}]没有数据了，共消费[{}]条数据", redisKey, totalConsume.get());
            return EMPTY;
        }
        Map.Entry<String, String> item;
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

    @Override
    public void open(@NonNull ExecutionContext executionContext) {
        if (scanOptions == null) {
            scanOptions = ScanOptions.scanOptions().match("*").count(count).build();
        }
        initThread();
    }

    private synchronized void initThread() {
        log.info("正在遍历[{}]的数据", redisKey);
        this.completableFuture = CompletableFuture.runAsync(() -> {
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            try (Cursor<Map.Entry<String, String>> cursor = operations.scan(redisKey, scanOptions)) {
                while (cursor.hasNext()) {
                    Map.Entry<String, String> next = cursor.next();
                    Object key = next.getKey();
                    queue.put(next);
                    total.incrementAndGet();
                    operations.delete(redisKey, key);
                }
                log.info("[{}]没有数据了，共获取到[{}]条数据", redisKey, total.get());
                stop = true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() {
        log.info("正在关闭遍历[{}]的数据", redisKey);
        completableFuture.complete(null);
        completableFuture.cancel(true);
    }
}
