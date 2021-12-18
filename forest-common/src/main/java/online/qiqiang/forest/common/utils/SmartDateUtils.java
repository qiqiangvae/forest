package online.qiqiang.forest.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 智能日期识别工具
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class SmartDateUtils {

    /**
     * 日期分隔符
     */
    private static final String DATE_SPLIT = "/";
    /**
     * 毫秒分隔符
     */
    private static final String MILL_SPLIT = ".";
    /**
     * 日期时间分隔符
     */
    private static final char DATETIME_SPLIT = 'T';
    /**
     * 区域
     */
    private static final String ZONE_SPLIT = "Z";
    /**
     * 日期长度限制
     */
    private static final int DATE_LIMIT = 10;


    /**
     * 文本得到LocalDate
     *
     * @param text 文本
     * @return 文本
     */
    public static LocalDate toLocalDate(final String text) {
        String string = text;
        if (text.length() > DATE_LIMIT) {
            string = text.substring(0, 10);
        }
        if (text.contains(DATE_SPLIT)) {
            return DateConvertor.parseToLocalDate(string, "yyyy/MM/dd");
        }
        return DateConvertor.parseToLocalDate(string, DateConvertor.Pattern.USUAL_DATE);
    }

    /**
     * 文本得到Date
     *
     * @param text 文本
     * @return Date
     */
    public static Date toDate(final String text) {
        if (text.length() > DATE_LIMIT) {
            return DateConvertor.localDateTimeToDate(toLocalDateTime(text));
        } else {
            return DateConvertor.localDateToDate(toLocalDate(text));
        }
    }

    /**
     * 文本得到LocalDateTime
     *
     * @param text 文本
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(final String text) {
        String string = text;
        // 处理带 "." 的情况
        if (text.contains(MILL_SPLIT)) {
            string = text.split("\\.", -1)[0];
        }
        if (string.length() > DATE_LIMIT && string.charAt(DATE_LIMIT) == DATETIME_SPLIT) {
            // 2020-02-02T00:00:00Z
            if (string.endsWith(ZONE_SPLIT)) {
                return LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC);
            } else {
                // 2020-02-02T00:00:00
                return LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        }
        // 2020-02-02 00:00:00
        return DateConvertor.parseToLocalDateTime(string, DateConvertor.Pattern.USUAL_DATE_TIME);
    }
}
