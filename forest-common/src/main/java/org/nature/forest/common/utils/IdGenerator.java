package org.nature.forest.common.utils;

import java.util.UUID;

/**
 * id 生成器
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class IdGenerator {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 雪花算法
     *
     * @return id
     */
    public static String snowFlask() {
        // todo 待实现
        return "";
    }
}