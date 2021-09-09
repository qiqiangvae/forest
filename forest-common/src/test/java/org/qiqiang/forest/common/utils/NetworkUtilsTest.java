package org.qiqiang.forest.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkUtilsTest {

    @Test
    public void available() {
        boolean available = NetworkUtils.available("localhost", 4049);
        System.out.println(available);
    }

    @Test
    public void testAvailable() {
        boolean available = NetworkUtils.available("127.0.0.1");
        System.out.println(available);
    }
}