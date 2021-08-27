package org.qiqiang.forest.common.utils.id;

/**
 * @author qiqiang
 */
public class IDGenerator {
    public static int getIdType(long id) {
        return SnowFlakeWrapper.getType(id);
    }

    public static long getLongId(int idType) {
        if (idType < IDTypeEnum.SYSTEM_CODE.getIdIndex() ||
                idType > IDTypeEnum.MAX_CODE.getIdIndex()) {
            return -1;
        }

        return SnowFlakeWrapper.getLongId(idType);
    }

    public static String getId(int codeType) {
        long longCode = IDGenerator.getLongId(codeType);
        if (longCode == -1) {
            return null;
        }

        return Long.toString(longCode);
    }
}
