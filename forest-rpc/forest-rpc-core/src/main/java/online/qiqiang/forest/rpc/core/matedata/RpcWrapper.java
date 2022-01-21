package online.qiqiang.forest.rpc.core.matedata;

import lombok.Data;
import online.qiqiang.forest.rpc.core.exception.RpcInvokeException;

import java.io.Serializable;

/**
 * @author qiqiang
 */
@Data
public class RpcWrapper implements Serializable {
    private ServiceMetaData metaData;
    private Object[] params;
    private Object response;
    private RpcInvokeException exception;
}
