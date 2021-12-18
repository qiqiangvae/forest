package online.qiqiang.forest.mvc.vo;

import lombok.Data;
import online.qiqiang.forest.common.exception.ErrorCode;
import online.qiqiang.forest.common.exception.IErrorCode;

/**
 * @author qiqiang
 */
@Data
@SuppressWarnings("unused")
public class FResponse<T> {
    private int code;
    private T data;
    private String message;

    public FResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public FResponse(T data) {
        this.data = data;
        this.code = ErrorCode.SUCCESS.getCode();
        this.message = ErrorCode.SUCCESS.getMessage();
    }

    public FResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static FResponse<String> code(IErrorCode errorCode) {
        return new FResponse<>(errorCode.getCode(), errorCode.getMessage());
    }
}
