package online.qiqiang.forest.common.utils;

import online.qiqiang.forest.common.exception.BlankException;
import org.apache.commons.lang3.StringUtils;

/**
 * 文本工具类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class Strings extends StringUtils {

    /**
     * 驼峰转蛇形
     *
     * @param camel 驼峰文本
     * @return 蛇形文本
     */
    public static String camelToSnake(String camel) {
        return camel;
    }

    /**
     * 如果字符串为空，则抛出异常
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String requireNonBlank(String str) {
        if (StringUtils.isBlank(str))
            throw new BlankException();
        return str;
    }

    public static String requireNonBlank(String str, String message) {
        if (StringUtils.isBlank(str))
            throw new BlankException(message);
        return str;
    }
}
