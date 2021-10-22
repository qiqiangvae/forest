package org.nature.forest.common.utils;

import org.junit.Test;

public class BatchUtilsTest {

    @Test
    public void execute() {
        long count = BatchUtils.execute(3, generator -> {
            for (int i = 0; i < 10; i++) {
                generator.add(new Object());
            }
        }, optionalCollection -> optionalCollection.isNotEmpty(System.out::println));
        System.out.println(count);
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