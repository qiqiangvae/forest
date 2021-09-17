package org.qiqiang.forest.common.utils;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void parse2DateTest() {
        LocalDate localDate = DateUtils.parseToLocalDate("2015-09-10", DateUtils.PATTERN_YYYY_MM_DD);
        System.out.println(localDate);
    }

    @Test
    public void parseToLocalDateTimeTest() {
        LocalDateTime localDateTime = DateUtils.parseToLocalDateTime("2015-09-10 00:01:01", DateUtils.PATTERN_FULL_DATE_TIME);
        System.out.println(localDateTime);
    }

    @Test
    public void parseToStringTest() {
        System.out.println(DateUtils.parseToString(new Date(), DateUtils.PATTERN_FULL_DATE_TIME));
        System.out.println(DateUtils.parseToString(LocalDateTime.now(), DateUtils.PATTERN_FULL_DATE_TIME));
        System.out.println(DateUtils.parseToString(LocalDate.now(), DateUtils.PATTERN_FULL_DATE_TIME));
    }
}