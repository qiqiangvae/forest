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
