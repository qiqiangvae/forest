package org.nature.forest.common.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void parse2DateTest() {
        LocalDate localDate = DateConvertor.parseToLocalDate("2015-09-10", DateConvertor.Pattern.USUAL_DATE);
        System.out.println(localDate);
    }

    @Test
    public void parseToLocalDateTimeTest() {
        LocalDateTime localDateTime = DateConvertor.parseToLocalDateTime("2015-09-10 00:01:01", DateConvertor.Pattern.USUAL_DATE_TIME);
        System.out.println(localDateTime);
    }

    @Test
    public void parseToStringTest() {
        System.out.println(DateConvertor.parseToString(new Date(), DateConvertor.Pattern.USUAL_DATE_TIME));
        System.out.println(DateConvertor.parseToString(LocalDateTime.now(), DateConvertor.Pattern.USUAL_DATE_TIME));
        System.out.println(DateConvertor.parseToString(LocalDate.now(), DateConvertor.Pattern.USUAL_DATE_TIME));
    }
}