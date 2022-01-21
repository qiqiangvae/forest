package online.qiqiang.forest.rpc.common.protocol;

import lombok.Getter;
import lombok.Setter;
import online.qiqiang.forest.rpc.common.consts.MessageTypeEnum;
import online.qiqiang.forest.rpc.common.serializer.SerializerManager;

/**
 * 4+1+8+32+4+8+length
 * @author qiqiang
 */
public class ForestRpcProtocol {
    @Getter
    private static final int startFlag = 0x527;
    @Getter
    @Setter
    private byte version;
    @Getter
    @Setter
    private Long sessionId;
    /**
     * 32 位
     */
    @Getter
    @Setter
    private String requestId;
    @Getter
    @Setter
    private MessageTypeEnum messageType;
    @Getter
    private long bodyLength;
    @Getter
    private byte[] body;

    public void setBody(Object body) {
        setBody(SerializerManager.getSerializer().serialize(body));
    }

    public void setBody(byte[] body) {
        this.body = body;
        bodyLength = body.length;
    }
}
