package online.qiqiang.forest.common.utils;

import junit.framework.TestCase;

public class CompareUtilsTest extends TestCase {

    public void testMax() {
        CompareUtils.max(10,12).ifPresent(System.out::println);
    }

    public void testMin() {
        CompareUtils.min(10,12).ifPresent(System.out::println);
    }
}