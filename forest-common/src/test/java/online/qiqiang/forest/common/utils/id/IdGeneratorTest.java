package online.qiqiang.forest.common.utils.id;

import org.junit.Test;
import online.qiqiang.forest.common.utils.AssertUtils;
import online.qiqiang.forest.common.utils.OsUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qiqiang
 */
public class IdGeneratorTest {
    @Test
    public void snowFlakeTest() throws InterruptedException {
        Set<Long> set = Collections.synchronizedSet(new HashSet<>());
        int count = 100;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(OsUtils.getAvailableProcessors() * 2);
        int loopCount = 1000;
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                for (int i1 = 0; i1 < loopCount; i1++) {
                    set.add(IdGenerator.snowFlask(100));
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        AssertUtils.isTure(count * loopCount == set.size(), "有重复ID");
    }

}
