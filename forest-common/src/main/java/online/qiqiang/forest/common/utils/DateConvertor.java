package online.qiqiang.forest.common.utils;

import online.qiqiang.forest.common.java.util.function.ExFunction;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期、时间转换工具类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class DateConvertor {

    private static final Map<String, DateTimeFormatter> FORMATTER_MAP = new HashMap<>();

    static {
        Field[] fields = Pattern.class.getFields();
        Arrays.stream(fields)
                .map((ExFunction<Field, String>) field -> (String) field.get(null))
                .forEach(o -> FORMATTER_MAP.put(o, DateTimeFormatter.ofPattern(Pattern.USUAL_DATE)));
    }

    public interface Pattern {
        String USUAL_DATE = "yyyy-MM-dd";
        String USUAL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * 字符串解析成 Date
     */
    public static Date parseToDate(String text, String pattern) {
        return localDateToDate(parseToLocalDate(text, pattern));
    }

    /**
     * 字符串解析成 LocalDate
     */
    public static LocalDate parseToLocalDate(String text, String pattern) {
        DateTimeFormatter formatter = getDateTimeFormatter(pattern);
        return LocalDate.parse(text, formatter);
    }

    /**
     * Date 转 LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 字符串转 LocalDateTime
     */
    public static LocalDateTime parseToLocalDateTime(String text, String pattern) {
        DateTimeFormatter formatter = getDateTimeFormatter(pattern);
        return LocalDateTime.parse(text, formatter);
    }

    /**
     * Date 转 LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDate 转 Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime 转 Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 解析成字符串
     */
    public static String format(Date date, String pattern) {
        DateTimeFormatter formatter = getDateTimeFormatter(pattern);
        return formatter.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    /**
     * LocalDateTime 解析成字符串
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = getDateTimeFormatter(pattern);
        return formatter.format(localDateTime);
    }

    /**
     * LocalDate 解析成字符串
     */
    public static String format(LocalDate localDate, String pattern) {
        DateTimeFormatter formatter = getDateTimeFormatter(pattern);
        return formatter.format(localDate.atStartOfDay());
    }

    /**
     * @param date date
     * @return milliseconds
     */
    public static long timestamp(Date date) {
        return date.getTime();
    }

    public static Date dateFrom(long timestamp) {
        return Date.from(Instant.ofEpochMilli(timestamp));
    }

    public static LocalDateTime localDateTimeFrom(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static DateTimeFormatter getDateTimeFormatter(String pattern) {
        return FORMATTER_MAP.computeIfAbsent(pattern, key -> DateTimeFormatter.ofPattern(pattern));
    }

}
