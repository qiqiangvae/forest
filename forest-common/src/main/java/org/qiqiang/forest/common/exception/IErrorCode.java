package org.nature.forest.common.exception;

/**
 * @author qiqiang
 */
public interface IErrorCode {
    /**
     * 错误码
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 信息
     *
     * @return 信息
     */
    String getMessage();
}
