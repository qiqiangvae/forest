package org.qiqiang.forest.query;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author qiqiang
 */
@Getter
@Setter
@Accessors(chain = true)
public class ConditionWrapper {
    /**
     * 表达式
     */
    private Express express;
    /**
     * 列名
     */
    private String col;

}
