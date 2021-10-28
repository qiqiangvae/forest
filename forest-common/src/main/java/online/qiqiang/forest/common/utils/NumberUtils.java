package online.qiqiang.forest.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * 数字工具类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class NumberUtils {

    private static final Random RND = new Random();

    public static int getRandomInt(int min, int max) {
        return (int) Math.round((Math.random() * (max - min)) + min);
    }

    public static Integer parseInt(String in) {
        return parseInt(in, 0);
    }

    public static Integer parseInt(String in, Integer defaultValue) {
        Integer re = defaultValue;
        if (!StringUtils.isEmpty(in)) {
            try {
                re = Integer.parseInt(in);
            } catch (Exception ignore) {
            }
        }
        return re;
    }

    public static Long parseLong(String in) {
        return parseLong(in, 0L);
    }

    public static Long parseLong(String in, Long defaultValue) {
        Long re = defaultValue;
        if (!StringUtils.isEmpty(in)) {
            try {
                re = Long.parseLong(in);
            } catch (Exception ignore) {
            }
        }
        return re;
    }

    public static Double parseDouble(String in) {
        return parseDouble(in, 0D);
    }

    public static Double parseDouble(String in, Double defaultValue) {
        Double re = defaultValue;
        if (!StringUtils.isEmpty(in)) {
            try {
                re = Double.parseDouble(in);
            } catch (Exception ignore) {
            }
        }
        return re;
    }

    public static int[] parseInt(String[] in) {
        int[] arr = new int[in.length];
        int i = 0;
        for (String s : in) {
            arr[i] = parseInt(s);
            i++;
        }
        return arr;
    }

    /**
     * 生成固定长度的随机数
     *
     * @param digCount digCount
     * @return String
     */
    public static String getRandomNumberWithFixLength(int digCount) {
        StringBuilder sb = new StringBuilder(digCount);
        for (int i = 0; i < digCount; i++) {
            sb.append((char) ('0' + RND.nextInt(10)));
        }
        return sb.toString();
    }

    /**
     * 除法，四舍五入
     *
     * @param scale    保留几位小数
     * @param dividend 被除数
     * @param divisor  除数
     * @return Double
     */
    public static Double divide(int scale, Object dividend, Object divisor) {
        BigDecimal ret;
        try {
            if (dividend instanceof Long) {
                ret = BigDecimal.valueOf((Long) dividend);
            } else if (dividend instanceof Double) {
                ret = BigDecimal.valueOf((Double) dividend);
            } else if (dividend instanceof Float) {
                ret = BigDecimal.valueOf((Float) dividend);
            } else if (dividend instanceof String) {
                ret = BigDecimal.valueOf(NumberUtils.parseDouble((String) dividend));
            } else {
                return 0D;
            }
            if (divisor instanceof Long) {
                ret = ret.divide(BigDecimal.valueOf((Long) divisor), scale, RoundingMode.HALF_UP);
            } else if (divisor instanceof Double) {
                ret = ret.divide(BigDecimal.valueOf((Double) divisor), scale, RoundingMode.HALF_UP);
            } else if (dividend instanceof Float) {
                ret = ret.divide(BigDecimal.valueOf((Float) divisor), scale, RoundingMode.HALF_UP);
            } else if (divisor instanceof String) {
                ret = ret.divide(BigDecimal.valueOf(NumberUtils.parseDouble((String) divisor)), scale, RoundingMode.HALF_UP);
            } else {
                return 0D;
            }
        } catch (Exception e) {
            return 0D;
        }
        return ret.doubleValue();
    }
}
