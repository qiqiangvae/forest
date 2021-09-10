package org.qiqiang.forest.common.utils;

import java.util.UUID;

/**
 * id 生成器
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class IdGenerator {

    @SuppressWarnings("unused")
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @SuppressWarnings("unused")
    public static String snowFlask() {
        // todo 待实现
        return "";
    }
}
