package org.qiqiang.forest.query;

/**
 * @author qiqiang
 */
public enum Express {
    /**
     * 等于
     */
    equals,
    /**
     * 不等于
     */
    not_equals,
    /**
     * in
     */
    in,
    /**
     * 区间
     */
    between,
    /**
     * 大于
     */
    gt,
    /**
     * 大于等于
     */
    gte,
    /**
     * 小于
     */
    lt,
    /**
     * 小于等于
     */
    lte,
    /**
     * 左右模糊
     */
    like,
    /**
     * 左模糊
     */
    left_like,
    /**
     * 右模糊
     */
    right_like,
    ;

}
