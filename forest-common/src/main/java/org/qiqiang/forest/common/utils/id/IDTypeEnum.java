package org.qiqiang.forest.common.utils.id;

/**
 * @author qiqiang
 */
public enum IDTypeEnum {
    /**
     * 系统
     */
    SYSTEM_CODE("system", 0),
    /**
     * 预留最大值
     */
    MAX_CODE("max", 255);

    private String idRemark = null;
    private int idIndex;

    IDTypeEnum() {
        this.idIndex = 1 << ordinal();
    }

    IDTypeEnum(final String id, int index) {
        this.idRemark = id;
        this.idIndex = index;
    }

    public final String getIdRemark() {
        return this.idRemark;
    }

    public final int getIdIndex() {
        return this.idIndex;
    }
}
