package online.qiqiang.forest.rpc.core.exception;

import online.qiqiang.forest.common.exception.BaseForestException;

/**
 * @author qiqiang
 */
public class RpcInvokeException extends BaseForestException {
    public RpcInvokeException() {
    }

    public RpcInvokeException(String message) {
        super(message);
    }

    public RpcInvokeException(Throwable cause) {
        super(cause);
    }
}
