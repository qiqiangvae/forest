package org.qiqiang.forest.common.utils;

import java.util.UUID;

/**
 * id 生成器
 *
 * @author qiqiang
 */
public class IdGenerator {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String snowFlask() {
        // todo 待实现
        return "";
    }
}
