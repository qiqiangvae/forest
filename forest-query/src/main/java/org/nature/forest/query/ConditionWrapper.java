package org.nature.forest.query;

import lombok.Getter;

/**
 * @author qiqiang
 */
@Getter
public class ConditionWrapper {
    /**
     * 表达式
     */
    private final Express express;
    /**
     * 列名
     */
    private final String col;

    public ConditionWrapper(Express express, String col) {
        this.express = express;
        this.col = col;
    }
}
