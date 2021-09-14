package org.qiqiang.forest.common.exception;

import lombok.Getter;

/**
 * 通用业务异常
 *
 * @author qiqiang
 */
@Getter
@SuppressWarnings("unused")
public class BusinessException extends BaseForestException {
    /**
     * 错误码
     */
    private final int code;
    /**
     * 信息
     */
    private final String message;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(IErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public static BusinessException wrap(IErrorCode errorCode) {
        return new BusinessException(errorCode);
    }
}
