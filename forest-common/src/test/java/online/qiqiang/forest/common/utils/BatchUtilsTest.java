package online.qiqiang.forest.common.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/qiqiangvae/forest/blob/main/forest-common/src/main/java/org/nature/forest/common/java/util/OptionalCollection.java"></a>
 */
public class BatchUtilsTest {

    @Test
    public void withoutBatchUtils() {
        List<Object> list = new ArrayList<>(10);
        long count = 0;
        for (int i = 0; i < 99; i++) {
            list.add(new Object());
            count++;
            if (list.size() == 10) {
                System.out.println("插入数据" + list);
                list.clear();
            }
        }
        if (!list.isEmpty()) {
            System.out.println("插入数据" + list);
        }
        System.out.println("共处理" + count);
    }

    @Test
    public void execute() {
        long count = BatchUtils.execute(9, generator -> {
            for (int i = 0; i < 99; i++) {
                generator.add(new Object());
            }
        }, optionalCollection -> optionalCollection.isNotEmpty(System.out::println));
        System.out.println("共处理" + count);
    }

    @Test
    public void execute2() {
        long l = BatchUtils.execute(3, generator -> {
            for (int i = 0; i < 100; i++) {
                if (i % 3 == 0) {
                    generator.add(new Object());
                } else if (i % 3 == 1) {
                    generator.add(i);
                } else {
                    generator.add(i);
                }
            }
        }, BatchUtils.Factory.builder()
                .add(clazz -> clazz.equals(Object.class), optionalCollection -> optionalCollection.isNotEmpty(System.out::println))
                .add(clazz -> clazz.equals(String.class), optionalCollection -> optionalCollection.isNotEmpty(System.out::println))
                .add(clazz -> clazz.equals(Integer.class), optionalCollection -> optionalCollection.isNotEmpty(System.out::println))
                .add(clazz -> true, optionalCollection -> optionalCollection.isNotEmpty(item -> System.out.println("全量打印" + item)))
                .build());
        System.out.println(l);
    }
}