package online.qiqiang.forest.rpc.core.consumer;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiqiang
 */
@Getter
@Setter
public class RpcResponseObject {
    private final long startTime;

    public RpcResponseObject() {
        this.startTime = System.currentTimeMillis();
    }
}
