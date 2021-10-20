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
}