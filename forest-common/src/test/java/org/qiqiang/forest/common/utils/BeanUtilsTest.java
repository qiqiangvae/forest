package org.qiqiang.forest.common.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BeanUtilsTest extends BaseTest {

    @Test
    public void copyListTest() {
        int count = 100_0000;
        List<ClassA> list = new ArrayList<>(count);
        ClassA classA;
        for (int i = 0; i < count; i++) {
            classA = new ClassA();
            classA.setAge(i)
                    .setName("name" + i)
                    .setMoney(new BigDecimal(i));
            list.add(classA);
        }
        StopWatch stopWatch = StopWatch.createStarted();
        List<ClassB> result = BeanUtils.copy(list, ClassB.class);
        assertEquals("copy error", count, result.size());
        stopWatch.stop();
        System.out.println(stopWatch.getTime(TimeUnit.MICROSECONDS));
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class ClassA {
        String name;
        int age;
        BigDecimal money;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    static class ClassB {
        String name;
        int age;
        BigDecimal money;
    }
}