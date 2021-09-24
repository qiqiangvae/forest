package org.nature.forest.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期、时间转换工具类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class DateUtils {
    public static final String PATTERN_USUAL_DATE = "yyyy-MM-dd";
    public static final String PATTERN_USUAL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static Date parseToDate(String text, String pattern) {
        return localDateToDate(parseToLocalDate(text, pattern));
    }

    public static LocalDate parseToLocalDate(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(text, formatter);
    }

    public static LocalDateTime parseToLocalDateTime(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(text, formatter);
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String parseToString(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    public static String parseToString(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(localDateTime);
    }

    public static String parseToString(LocalDate localDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(localDate.atStartOfDay());
    }

    /**
     * @param date date
     * @return milliseconds
     */
    public static long timestamp(Date date) {
        return date.getTime();
    }

}
