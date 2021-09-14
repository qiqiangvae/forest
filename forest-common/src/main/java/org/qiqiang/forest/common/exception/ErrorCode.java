package org.qiqiang.forest.common.exception;

/**
 * 通用错误码
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public enum ErrorCode implements IErrorCode {
    /**
     * 成功
     */
    SUCCESS(2000, "SUCCESS"),

    /**
     * 参数异常
     */
    PARAMS_ERROR(4000, "参数异常"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(5000, "系统异常");

    /**
     * 错误码
     */
    int code;
    /**
     * 信息
     */
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
