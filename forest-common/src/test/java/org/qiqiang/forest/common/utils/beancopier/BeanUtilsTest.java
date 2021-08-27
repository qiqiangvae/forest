package org.qiqiang.forest.common.utils.beancopier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.qiqiang.forest.common.utils.BaseTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BeanUtilsTest extends BaseTest {

    @Test
    public void copyListTest() {
        int count = 100_0000;
        List<ClassA> list = new ArrayList<>(count);
        ClassA classA;
        for (int i = 0; i < count; i++) {
            classA = new ClassA();
            classA.setAge(i);
            classA.setName("name" + i);
            classA.setMoney(new BigDecimal(i));
            list.add(classA);
        }
        StopWatch stopWatch = StopWatch.createStarted();
        List<ClassB> result = BeanUtils.copy(list, ClassB.class);
        assertEquals("copy error", count, result.size());
        stopWatch.stop();
        System.out.println(stopWatch.getNanoTime());
    }

    @Getter
    @Setter
    static class ClassA {
        String name;
        int age;
        BigDecimal money;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class ClassB {
        String name;
        int age;
        BigDecimal money;

    }
}