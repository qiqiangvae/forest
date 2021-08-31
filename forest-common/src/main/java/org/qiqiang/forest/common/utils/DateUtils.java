package org.qiqiang.forest.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author qiqiang
 */
public class DateUtils {
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    public static Date parse2Date(String text) {
        return localDate2Date(parse2LocalDate(text));
    }

    public static LocalDate parse2LocalDate(String text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD);
        return LocalDate.parse(text, formatter);
    }

    public static Date localDate2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
