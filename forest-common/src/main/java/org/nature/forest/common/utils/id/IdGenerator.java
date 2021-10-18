package org.nature.forest.common.utils.id;

import java.util.UUID;

/**
 * id 生成器
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class IdGenerator {
    private static final int SYSTEM_CODE = 0;
    private static final int MAX_CODE = 255;

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String shortUuid() {
        return uuid().substring(8);
    }

    /**
     * 雪花算法
     *
     * @param idType id 类型
     * @return id
     */
    public static long snowFlask(int idType) {
        if (idType < SYSTEM_CODE ||
                idType > MAX_CODE) {
            return -1;
        }
        return SnowFlakeWrapper.getLongId(idType);
    }
}
