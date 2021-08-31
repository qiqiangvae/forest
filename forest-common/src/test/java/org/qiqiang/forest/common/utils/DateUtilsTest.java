package org.qiqiang.forest.common.utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void parse2DateTest() {
        LocalDate localDate = DateUtils.parse2LocalDate("2015-09-10");
        System.out.println(localDate);
    }
}